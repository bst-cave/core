package io.bst.stats

import akka.actor.{Actor, ActorLogging}
import io.bst.contentprovider.ContentProviderActor.{Content, ContentProviderInfo}
import io.bst.stats.StatsActor.Operation.Operation
import io.bst.user.User
import java.time.Instant


/**
 * Protocol for ``StatsActor``
 */
object StatsActor {

  object Operation extends Enumeration {
    type Operation = Value
    val Created = Value("created")
    val Updated = Value("updated")
    val Removed = Value("removed")
    val Undefined = Value("undefined")
  }

  /**
   * Signals a successfully indexed content item
   *
   * @param provider the provider which created the content
   * @param content the content
   * @param timestamp a timestamp when the indexing happened
   * @param operation whether the content was indexed for the first time or updated
   */
  case class IndexedContent(user: User, provider: ContentProviderInfo, content: Content, timestamp: Instant, operation: Operation)

  /**
   * Signals a successfully indexed pile of content items
   * @param pile the indexed content items
   */
  case class IndexedPile(pile: Seq[IndexedContent])

}

/**
 * An actor which uses Cassandra to store statistics of indexed content
 */
class StatsActor extends Actor with ActorLogging {

  import StatsActor._

  override def receive = {

    case IndexedContent(user, provider, content, timestamp, operation) =>
      log.info("{} {} for {} @ {}", operation, s"$content from $provider", user, timestamp)

    case IndexedPile(pile) =>
      log.info("Indexed a pile of {} content items", pile.length)
      pile.foreach(item => log.info("{} {} for {} @ {}", item.operation, s"$item.content from $item.provider", item.user, item.timestamp))
  }
}
