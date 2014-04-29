package io.bst

import akka.actor.ActorSystem
import akka.event.Logging
import akka.io.IO
import akka.pattern.ask
import java.util.UUID
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import spray.can.Http
import spray.client.pipelining._
import spray.http._
import spray.util._

object Boot extends App {
  implicit val system = ActorSystem("bst-core")

  import system.dispatcher

  val log = Logging(system, getClass)
  log.info("Requesting spray.io")

  val clientId = "914945663646-p00979sopuk3nv4v7bi8ek8359oppd3k.apps.googleusercontent.com"
  val clientSecret = "kCVASP8wkHq6y6eFEq_iYPLT"
  val state = UUID.randomUUID().toString
  val params = Map("client_id" -> clientId,
    "response_type" -> "code",
    "scope" -> "https://www.googleapis.com/auth/plus.login",
    "redirect_uri" -> "http://127.0.0.1:8080/oauth/cb/google",
    "state" -> state,
    "access_type" -> "offline")
  val uri = Uri("https://accounts.google.com/o/oauth2/auth?" + params.map {
    case (key, value) => s"$key=$value"
  }.mkString("&"))

  val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
  pipeline(Get("http://spray.io/")) onComplete {
    case Success(httpResponse) =>
      log.info("Status: {}", httpResponse.status)
      shutdown()

    case Failure(error) =>
      log.error(error, "Couldn't get elevation")
      shutdown()
  }

  def shutdown(): Unit = {
    IO(Http).ask(Http.CloseAll)(1.second).await
    system.shutdown()
  }

  //  val api = system.actorOf(Props[Api], "api")
  //  IO(Http) ! Http.Bind(api, interface = "localhost", port = 8080)

  //  val es = ElasticSearch(ElasticClient.local, User(randomUUID, "foobar", "foo@bar.com"))
  //  val filename = new File(".").getCanonicalPath + "/random.txt"
  //  val fileProvider = system.actorOf(FileProvider.props(filename, es), "fileProvider")
  //  QuartzSchedulerExtension(system).schedule("Every10Seconds", fileProvider, Tick)
}
