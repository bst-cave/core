package io.bst.index

import akka.actor.Actor
import io.bst.contentprovider.Content
import java.time.Instant

/**
 * Contains the messages, the indexer actor sends.
 * @author Harald Pehl
 */
object Indexer {
  case class Indexed(content: Content, indexAt: Instant)
}

/**
 * An actor which indexes content
 * @author Harald Pehl
 */
class Indexer extends Actor {
  import Indexer._
  override def receive: Receive = {
    case content: Content =>
      // TODO Index content
      sender() ! Indexed(content, Instant.now())
  }
}
