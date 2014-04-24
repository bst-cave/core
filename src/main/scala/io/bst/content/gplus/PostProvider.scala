package io.bst.content.gplus

import akka.actor.{ActorLogging, Props, Actor}
import io.bst.content.{ContentProvider, ContentProviderActor}
import io.bst.ext.ElasticSearch
import io.bst.index.Indexer
import io.bst.stats.Stats
import io.bst.model.Protocol.Tick
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential


object PostProvider {
  def props(filename: String, es: ElasticSearch): Props = Props(new PostProvider(es))
}

class PostProvider(es: ElasticSearch) extends Actor with ActorLogging with ContentProviderActor {

  val provider: ContentProvider = ContentProvider(getClass.getName, "Google+ Post Provider", Some("Provides G+ posts"))
  val indexer = context.actorOf(Indexer.props(es), "indexer")
  val stats = context.actorOf(Props[Stats], "stats")

  val httpTransport = new NetHttpTransport()

  override def receive = {
    case Tick =>
      log.debug("Collect all G+ posts")
      new GoogleCredential.Builder()
        .setTransport(httpTransport)
        .setJsonFactory()
  }
}
