package io.bst.index

import akka.actor.{ActorLogging, Props, Actor}
import java.time.Instant
import io.bst.model.Protocol.{IndexPile, Updated, IndexContent, Indexed}
import scala.collection.JavaConversions._
import io.bst.ext.ElasticSearch
import scala.util.{Failure, Success}
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.update.UpdateResponse
import org.elasticsearch.action.ActionResponse


object Indexer {
  /**
   * Create Props for an actor of this type.
   * @param es The ElasticSearch singleton.
   * @return a Props for creating this actor, which can then be further configured
   *         (e.g. calling `.withDispatcher()` on it)
   */
  def props(es: ElasticSearch): Props = Props(new Indexer(es))
}


/**
 * An actor which indexes content to an ElasticSearch index. The sender is notified with [[Indexed]] or [[Updated]]
 * messages.
 * @author Harald Pehl
 */
class Indexer(es: ElasticSearch) extends Actor with ActorLogging {

  import context.dispatcher

  override def receive = {
    case IndexContent(provider, content) =>
      log.debug("Going to index [{}] from {}", content, provider)
      val currentSender = sender()
      es.index(provider, content) onComplete {
        case Success(response) =>
          if (response.isCreated) currentSender ! Indexed(content, provider, Instant.now())
          else currentSender ! Updated(content, provider, Instant.now())
        case Failure(ex) => log.error(ex, "Unable to index [{}] from {}", content, provider)
      }

    case IndexPile(provider, pile) =>
      log.debug("Going to index a pile of {} content items from {}", pile.length, provider)
      val currentSender = sender()
      es.index(provider, pile) onComplete {
        case Success(response) => response.zipWithIndex foreach {
          case (item, index) => item.getResponse[ActionResponse] match {
            case ir: IndexResponse => currentSender ! Indexed(pile(index), provider, Instant.now())
            case ur: UpdateResponse =>
              if (ur.isCreated) currentSender ! Indexed(pile(index), provider, Instant.now())
              else currentSender ! Updated(pile(index), provider, Instant.now())
            case unknown => log.warning("Unknown type '{}' in response to index a pile of {} content items from {}",
              unknown.getClass.getName, pile.length, provider)
          }
        }
        case Failure(ex) => log.error(ex, "Unable to index a pile of {} content items from {}", pile.length, provider)
      }
  }
}
