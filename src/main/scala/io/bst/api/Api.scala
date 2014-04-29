package io.bst.api

import akka.actor.Props
import io.bst.core.{CoreActors, Core}
import spray.routing.RouteConcatenation

/**
 * The REST API layer. It defines the routes and as such exposes the REST services, but does not provide any
 * web server interface.<br/>
 * Notice that it requires to be mixed in with ``core.CoreActors``, which provides access to the top-level actors
 * that make up the system.
 */
trait Api extends RouteConcatenation {
  this: CoreActors with Core =>

  private implicit val _ = system.dispatcher

  val routes = _

  val rootService = system.actorOf(Props(new RoutedHttpService(routes)))
}
