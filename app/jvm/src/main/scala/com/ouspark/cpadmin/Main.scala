package com.ouspark.cpadmin

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration.{Duration, FiniteDuration}

object Main extends App with RequestTimeOut {

  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system: ActorSystem = ActorSystem("cpadmin")
  implicit val ex: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val routes =
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    }

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, host, port)

  val log = Logging(system.eventStream, "cpadmin")
  bindingFuture.map { serverBinding =>
    log.info(s"API bound to ${serverBinding.localAddress}")
  }.onComplete {
    case ex: Exception =>
      log.error(ex, "Fail to bind to {}:{}", host, port)
      system.terminate()
    case _ => system.terminate()
  }
}

trait RequestTimeOut {
  def requestTimeOut(config: Config): Timeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
