package com.ouspark.cpadmin

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
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

  val staticResources =
    (get & pathPrefix("cpadmin")){
      (pathEndOrSingleSlash & redirectToTrailingSlashIfMissing(StatusCodes.TemporaryRedirect)) {
        getFromResource("cpadmin/index.html")
      } ~  {
        getFromResourceDirectory("cpadmin")
      }
    }

  val routes =
    path("todos") {
      get {
        complete(HttpEntity(ContentTypes.`application/json`, """[{"title":"Login page with credentials","prior":{"liClass":"list-warning","spanClass":"label-warning"}},{"title":"Figure out how Publisher deploy and connect with credentials","prior":{"liClass":"list-danger","spanClass":"label-danger"},"label":"2 Day"},{"title":"Publisher API service test out","prior":{"liClass":"list-info","spanClass":"label-info"},"label":"API"},{"title":"Task list first catch","prior":{"liClass":"list-warning","spanClass":"label-warning"},"label":"Task"},{"title":"Task Summary","prior":{"liClass":"list-info","spanClass":"label-info"},"label":"Summary"},{"title":"Task Save implementation","prior":{"liClass":"list-danger","spanClass":"label-danger"},"label":"Functionality"}]"""))
      }
    } ~ staticResources


  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, host, port)

  val log = Logging(system.eventStream, "cpadmin")
  bindingFuture.map { serverBinding =>
    log.info(s"API bound to ${serverBinding.localAddress}")
  }.onFailure {
    case ex: Exception =>
      log.error(ex, "Fail to bind to {}:{}", host, port)
      system.terminate()
  }

}

trait RequestTimeOut {
  def requestTimeOut(config: Config): Timeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
