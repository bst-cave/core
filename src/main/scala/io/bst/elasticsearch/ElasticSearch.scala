package io.bst.elasticsearch

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import io.bst.contentprovider.ContentProviderActor.{Content, ContentProviderInfo}
import io.bst.user.User
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.update.UpdateResponse
import scala.Some
import scala.concurrent.Future


class ElasticSearch(elasticClient: ElasticClient) {

  val indexName = (user: User) => user.username.toString
  val indexType = (user: User) => indexName(user) -> "bst"

  def createIndex(user: User): Future[CreateIndexResponse] = elasticClient execute {
    create index indexName(user)
  }

  def upsert(user: User, provider: ContentProviderInfo, content: Content): Future[UpdateResponse] = elasticClient execute {
    update(content.id) in indexType(user) docAsUpsert documentFor(provider, content)
  }

  def upsert(user: User, provider: ContentProviderInfo, pile: Seq[Content]): Future[BulkResponse] = {
    val updateDefinitions = pile map {
      content => update(content.id) in indexType(user) docAsUpsert documentFor(provider, content)
    }
    elasticClient bulk (updateDefinitions: _*)
  }

  private def documentFor(provider: ContentProviderInfo, content: Content) = {
    val basics = Map("id" -> content.id, "url" -> content.url, "excerpt" -> content.excerpt, "tags" -> content.tags,
      "provider" -> provider.id)
    content match {
      case Content(_, _, _, None, _) => basics
      case Content(_, _, _, Some(data), _) => basics + ("data" -> data)
    }
  }
}
