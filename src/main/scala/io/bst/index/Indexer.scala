package io.bst.index

import akka.actor.{ActorLogging, Props, Actor}
import io.bst.ext.ElasticSearch
import io.bst.model.Protocol.{IndexedPile, IndexContent, IndexPile, IndexedContent}
import io.bst.model.Protocol.IndexedContent.Operation
import io.bst.model.Protocol.IndexedContent.Operation.Operation
import java.time.Instant
import org.elasticsearch.action.bulk.BulkItemResponse
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.update.UpdateResponse
import org.elasticsearch.common.io.stream.Streamable


object Indexer {
  def props(es: ElasticSearch): Props = Props(new Indexer(es))
}


/**
 * An actor which indexes content to an ElasticSearch index. The sender is notified with an [[IndexedContent]] message.
 * @author Harald Pehl
 */
class Indexer(es: ElasticSearch) extends Actor with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  override def receive = {

    case IndexContent(provider, content) =>
      log.debug("Going to index {} from {}", content, provider)
      es.index(provider, content) map {
        response => IndexedContent(provider, content, Instant.now(), operation(response))
      } pipeTo sender()

    case IndexPile(provider, pile) =>
      log.debug("Going to index a pile of {} content items from {}", pile.length, provider)
      es.index(provider, pile) map {
        response => response.getItems.toList.zipWithIndex filter {
          case (item, _) => !item.isFailed
        } map {
          case (item, index) => IndexedContent(provider, pile(index), Instant.now(), operation(item))
        }
      } map(IndexedPile(_)) pipeTo sender()
  }

  private def operation[T <: Streamable](response: T): Operation = {
    response match {
      case ir: IndexResponse => Operation.Created
      case ur: UpdateResponse => if (ur.isCreated) Operation.Created else Operation.Updated
      case bir: BulkItemResponse => operation(bir.getResponse)
      case _ => Operation.Undefined
    }
  }
}
