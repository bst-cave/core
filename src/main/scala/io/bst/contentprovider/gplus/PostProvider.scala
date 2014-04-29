package io.bst.contentprovider.gplus

import akka.actor.{ActorLogging, Props, Actor}
import io.bst.contentprovider.ContentProviderActor
import io.bst.env.ElasticSearch
import io.bst.index.IndexActor
import io.bst.model.Protocol.Tick
import io.bst.stats.StatsActor
import io.bst.contentprovider.{ContentProviderActor, ContentProvider}


object PostProvider {
  def props(filename: String, es: ElasticSearch): Props = Props(new PostProvider(es))
}

class PostProvider(es: ElasticSearch) extends Actor with ActorLogging with ContentProviderActor {

  val info: ContentProvider = ContentProvider(getClass.getName, "Google+ Post Provider", Some("Provides G+ posts"))
  val indexer = context.actorOf(IndexActor.props(es), "indexer")
  val stats = context.actorOf(Props[StatsActor], "stats")

  override def receive = {
    case Tick =>
      log.debug("Collect all G+ posts")
  }
}
