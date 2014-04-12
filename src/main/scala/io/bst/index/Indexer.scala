package io.bst.index

import akka.actor.Actor
import java.time.Instant
import io.bst.user.User
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import io.bst.model.Protocol.{Updated, Index, Indexed}
import io.bst.content.Content


/**
 * An actor which indexes content to an ElasticSearch index
 * @author Harald Pehl
 */
class Indexer(user: User) extends Actor {

  import context.dispatcher

  val uid = user.id.toString
  val client = ElasticClient.local

  client.execute(create index uid)

  override def receive = {
    case idxEntry: Index =>

      val idxId = idxEntry.content.id
      val basics = Map("url" -> idxEntry.content.url, "excerpt" -> idxEntry.content.excerpt, "tags" -> idxEntry.content.tags)
      val idxContent = idxEntry.content match {
        case Content(_, _, _, None, _) => basics
        case Content(_, _, _, Some(data), _) => basics + ("data" -> data)
      }

      client.execute(update(idxId) in (uid -> "bst") docAsUpsert idxContent) map {
        updateResponse =>
          if (updateResponse.isCreated) sender ! Indexed(idxEntry.content, idxEntry.provider, Instant.now())
          else sender ! Updated(idxEntry.content, idxEntry.provider, Instant.now())
      }
  }
}
