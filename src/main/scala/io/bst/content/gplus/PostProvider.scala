package io.bst.content.gplus

import akka.actor.{ActorLogging, Props, Actor}
import io.bst.content.{ContentProvider, ContentProviderActor}
import io.bst.ext.ElasticSearch
import io.bst.index.Indexer
import io.bst.model.Protocol.Tick
import io.bst.stats.Stats


object PostProvider {
  def props(filename: String, es: ElasticSearch): Props = Props(new PostProvider(es))
}

class PostProvider(es: ElasticSearch) extends Actor with ActorLogging with ContentProviderActor {

  val provider: ContentProvider = ContentProvider(getClass.getName, "Google+ Post Provider", Some("Provides G+ posts"))
  val indexer = context.actorOf(Indexer.props(es), "indexer")
  val stats = context.actorOf(Props[Stats], "stats")

  override def receive = {
    case Tick =>
      log.debug("Collect all G+ posts")
  }
}
