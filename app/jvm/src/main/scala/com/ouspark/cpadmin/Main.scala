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
import scala.io.StdIn

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
    staticResources ~
    pathPrefix("apis" / "v1") {
      path("todos") {
        get {
          complete(HttpEntity(ContentTypes.`application/json`, """[{"title":"Login page with credentials","prior":{"liClass":"list-warning","spanClass":"label-warning"}},{"title":"Figure out how Publisher deploy and connect with credentials","prior":{"liClass":"list-danger","spanClass":"label-danger"},"label":"2 Day"},{"title":"Publisher API service test out","prior":{"liClass":"list-info","spanClass":"label-info"},"label":"API"},{"title":"Task list first catch","prior":{"liClass":"list-warning","spanClass":"label-warning"},"label":"Task"},{"title":"Task Summary","prior":{"liClass":"list-info","spanClass":"label-info"},"label":"Summary"},{"title":"Task Save implementation","prior":{"liClass":"list-danger","spanClass":"label-danger"},"label":"Functionality"}]"""))
        }
      } ~
      path("feature" / Segment / Segment) { (typ, feature) =>
        get {
          complete(HttpEntity(ContentTypes.`application/json`, ""))
        }
      }
    }

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, host, port)

  val log = Logging(system.eventStream, "cpadmin")
  log.info(s"API bound to {} {}", host, port)
  StdIn.readLine()
  bindingFuture.flatMap(_.unbind())
    .onComplete(_ => system.terminate()) // shutdown when done

}

trait RequestTimeOut {
  def requestTimeOut(config: Config): Timeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
