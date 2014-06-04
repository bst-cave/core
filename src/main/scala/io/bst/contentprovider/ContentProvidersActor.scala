package io.bst.contentprovider

import akka.actor.{ActorRef, Props, Actor, ActorLogging}
import io.bst.contentprovider.sample.StaticProvider
import io.bst.contentprovider.ContentProviderActor.WhoAreYou


/**
 * Protocol for ``ContentProvidersActor``
 */
object ContentProvidersActor {

  case class List()

  def props(indexer: ActorRef): Props = Props(new ContentProvidersActor(indexer))
}


class ContentProvidersActor(indexer: ActorRef) extends Actor with ActorLogging {

  val contentProviders = List(context.actorOf(StaticProvider.props(indexer), "staticProvider"))

  override def receive = {
    case ContentProvidersActor.List => contentProviders.foreach(_ ! WhoAreYou)
  }
}
