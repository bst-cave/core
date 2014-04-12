package io.bst.index

import akka.actor.Actor
import java.time.Instant
import io.bst.user.User
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import io.bst.model.Protocol.{Index, Indexed}
import io.bst.content.Content


/**
 * An actor which indexes content to an ElasticSearch index
 * @author Harald Pehl
 */
class Indexer(user: User) extends Actor {

  val idxName = user.id.toString
  val client = ElasticClient.local
  client.execute {
    create index idxName
  }

  override def receive = {
    case idxEntry: Index =>

      // TODO Check which operation should applied CRUD?
      val basics = Map("url" -> idxEntry.content.url, "excerpt" -> idxEntry.content.excerpt, "tags" -> idxEntry.content.tags)
      val result = client.execute {
        index into idxName -> "bst" fields {
          idxEntry.content match {
            case Content(_, _, None, _) => basics
            case Content(_, _, Some(data), _) => basics + ("data" -> data)
          }
        }
      }

      result map {
        indexResponse => sender ! Indexed(idxEntry.content, idxEntry.provider, Instant.now())
      }
  }
}
