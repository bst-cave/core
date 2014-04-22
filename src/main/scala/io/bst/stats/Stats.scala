package io.bst.stats

import akka.actor.{Actor, ActorLogging}
import io.bst.model.Protocol.{IndexedPile, IndexedContent}

/**
 * An actor which uses Cassandra to store statistics of indexed content
 * @author Harald Pehl
 */
class Stats extends Actor with ActorLogging {

  override def receive = {

    case IndexedContent(provider, content, timestamp, operation) =>
      log.info("{} {} from {} @ {}", operation, content, provider, timestamp)

    case IndexedPile(pile) =>
      log.info("Indexed a pile of {} content items", pile.length)
      pile.foreach(item => log.info("{} {} from {} @ {}", item.operation, item.content, item.provider, item.timestamp))
  }
}
