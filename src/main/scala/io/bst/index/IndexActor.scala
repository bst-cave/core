package io.bst.index

import akka.actor.{ActorLogging, Props, Actor}
import akka.pattern.pipe
import io.bst.contentprovider.ContentProviderActor.{Content, ContentProviderInfo}
import io.bst.elasticsearch.ElasticSearch
import io.bst.stats.StatsActor.Operation._
import io.bst.stats.StatsActor.{IndexedPile, IndexedContent}
import io.bst.user.User
import java.time.Instant
import org.elasticsearch.action.bulk.BulkItemResponse
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.update.UpdateResponse
import org.elasticsearch.common.io.stream.Streamable


/**
 * Protocol for ``IndexActor``
 */
object IndexActor {

  sealed trait HasUser {
    def user: User
  }

  /**
   * Create an index for the given user
   */
  case class CreateIndex(user: User) extends HasUser

  /**
   * Index a pile of content items from a provider to an user index
   */
  case class IndexPile(user: User, provider: ContentProviderInfo, pile: Seq[Content]) extends HasUser

  /**
   * Index a single content item from a provider to an user index
   */
  case class IndexContent(user: User, provider: ContentProviderInfo, content: Content) extends HasUser

  def props(es: ElasticSearch): Props = Props(new IndexActor(es))
}


/**
 * An actor which indexes content to an ElasticSearch index.
 */
class IndexActor(es: ElasticSearch) extends Actor with ActorLogging {

  import IndexActor._
  import context.dispatcher

  val existingIndices = collection.mutable.Set[User]()

  override def receive = {

    case ic @ IndexContent(user, provider, content) =>
      ensureIndex(ic)
      log.debug("Going to index {} from {} for {}", content, provider, user)
      es.upsert(user, provider, content) map {
        response => IndexedContent(user, provider, content, Instant.now(), operation(response))
      } pipeToSelection context.actorSelection("/user/stats")

    case ip @ IndexPile(user, provider, pile) =>
      log.debug("Going to index a pile of {} content items from {} for {}", pile.length, provider, user)
      es.upsert(user, provider, pile) map {
        response => response.getItems.toList.zipWithIndex filter {
          case (item, _) => !item.isFailed
        } map {
          case (item, index) => IndexedContent(user, provider, pile(index), Instant.now(), operation(item))
        }
      } map (IndexedPile(_)) pipeToSelection context.actorSelection("/user/stats")
  }

  private def ensureIndex(hu: HasUser) {
    if (!existingIndices.contains(hu.user)) {
      log.debug("Create an index for {}", hu.user)
      es.createIndex(hu.user).map(_ => self ! hu)
    }
  }

  private def operation[T <: Streamable](response: T): Operation = {
    response match {
      case ir: IndexResponse => Created
      case ur: UpdateResponse => if (ur.isCreated) Created else Updated
      case bir: BulkItemResponse => operation(bir.getResponse)
      case _ => Undefined
    }
  }
}
