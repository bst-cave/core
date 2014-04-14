package io.bst.ext

import io.bst.user.User
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import io.bst.content.{ContentProvider, Content}
import scala.concurrent.Future
import org.elasticsearch.action.update.UpdateResponse
import org.elasticsearch.action.bulk.BulkResponse

/**
 * @author Harald Pehl
 */
object ElasticSearch {
  def apply(client: ElasticClient, user: User) = new ElasticSearch(client, user)
}

class ElasticSearch private(client: ElasticClient, user: User) {
  val uid = user.id.toString
  val indexName = uid -> "bst"

  client.sync.execute(create index uid)
  
  def index(provider: ContentProvider, content: Content): Future[UpdateResponse] = {
    client.execute(update(content.id) in indexName docAsUpsert documentFor(provider, content))
  }

  def index(provider: ContentProvider, pile: Seq[Content]): Future[BulkResponse] = {
    val updateDefinitions = pile.map(content => update(content.id) in indexName docAsUpsert documentFor(provider, content))
    client.bulk(updateDefinitions: _*)
  }

  private def documentFor(provider: ContentProvider, content: Content) = {
    val basics = Map("id" -> content.id, "url" -> content.url, "excerpt" -> content.excerpt, "tags" -> content.tags,
      "provider" -> provider.id)
    content match {
      case Content(_, _, _, None, _) => basics
      case Content(_, _, _, Some(data), _) => basics + ("data" -> data)
    }
  }
}
