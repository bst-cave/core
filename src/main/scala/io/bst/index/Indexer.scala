package io.bst.index

import akka.actor.Actor
import io.bst.contentprovider.Content
import java.time.Instant

case class Indexed(content: Content, indexAt: Instant)

/**
 * An actor which indexes content
 * @author Harald Pehl
 */
class Indexer(index: Index) extends Actor {

  override def receive: Receive = {
    case content: Content =>
      index push content map {
        indexResponse => sender ! Indexed(content, Instant.now())
      }
  }
}
