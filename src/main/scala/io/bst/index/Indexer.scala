package io.bst.index

import akka.actor.{Props, Actor}
import java.time.Instant
import io.bst.model.Protocol.{IndexPile, Indexed, IndexContent, Created}
import scala.collection.JavaConversions._
import io.bst.ext.ElasticSearch


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
 * An actor which indexes content to an ElasticSearch index. The sender is notified with [[Created]] or [[Indexed]]
 * messages.
 * @author Harald Pehl
 */
class Indexer(es: ElasticSearch) extends Actor {

  import context.dispatcher

  override def receive = {

    case IndexContent(provider, content) => es.index(provider, content) map {
      response =>
        if (response.isCreated) sender ! Created(content, provider, Instant.now())
        else sender ! Indexed(content, provider, Instant.now())
    }

    case IndexPile(provider, pile) => es.index(provider, pile) map {
      response => response.zipWithIndex foreach {
        case (item, index) => item.getOpType match {
          case "create" => sender ! Created(pile(index), provider, Instant.now())
          case "index" => sender ! Indexed(pile(index), provider, Instant.now())
        }
      }
    }
  }
}
