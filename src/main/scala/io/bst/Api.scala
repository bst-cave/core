package io.bst

import akka.actor.{ActorLogging, Actor}
import akka.util.Timeout
import scala.concurrent.duration._
import spray.can.Http
import spray.http.HttpMethods._
import spray.http.MediaTypes._
import spray.http._


/**
 * The REST interface
 */
class Api extends Actor with ActorLogging {

  implicit val timeout: Timeout = 1.second // for the actor 'asks'

  override def receive = {

    case _: Http.Connected => sender ! Http.Register(self)

    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      log.debug("Sending index...")
      sender ! index

    case _: HttpRequest => sender ! HttpResponse(status = 404, entity = "WTF!")
  }

  lazy val index = HttpResponse(
    entity = HttpEntity(`text/html`,
      <html>
        <body>
          <h1>Say hello to
            <i>BST</i>
            !</h1>
        </body>
      </html>.toString()
    )
  )
}
