package io.bst.contentprovider.gplus

import akka.actor.{ActorRef, ActorLogging, Props, Actor}
import io.bst.contentprovider.ContentProviderActor
import io.bst.contentprovider.ContentProviderActor.ContentProviderInfo


object PostProvider {
  def props(index: ActorRef): Props = Props(new PostProvider(index))
}

class PostProvider(index: ActorRef) extends Actor with ActorLogging with ContentProviderActor {

  override val info = ContentProviderInfo(getClass.getName, "G+ Post Provider")

  override def receive = {
    case _ => log.warning("Not yet implemented!")
  }
}
