package io.bst.index

import io.bst.contentprovider.Content
import io.bst.user.User
import scala.concurrent.Future
import org.elasticsearch.action.index.IndexResponse
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

/**
 * An ElasticSearch index capable of indexing [[Content]]
 * @author Harald Pehl
 */
class Index(user: User) {

  val idxName = user.id.toString
  val client = ElasticClient.local
  client.execute {
    create index idxName
  }

  def push(content: Content): Future[IndexResponse] = {
    val basics = Map("url" -> content.url, "excerpt" -> content.excerpt, "tags" -> content.tags)
    client.execute {
      index into idxName fields {
        content match {
          case Content(_, _, None, _) => basics
          case Content(_, _, Some(data), _) => basics + ("data" -> data)
        }
      }
    }
  }
}