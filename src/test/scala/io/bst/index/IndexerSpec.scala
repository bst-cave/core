package io.bst.index

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import io.bst.content.ContentFixture
import io.bst.ext.ElasticSearch
import io.bst.model.Protocol.IndexedContent.Operation
import io.bst.model.Protocol.{IndexedContent, IndexContent}
import io.bst.user.User
import java.util.UUID
import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpecLike}
import scala.concurrent.duration._
import scala.concurrent.{blocking, Await}


class IndexerSpec extends TestKit(ActorSystem("IndexerSpec"))
                          with ImplicitSender
                          with FlatSpecLike
                          with Matchers
                          with BeforeAndAfterAll {

  def withElasticSearch(testCode: (ElasticClient, ElasticSearch) => Any) {
    val client = ElasticClient.local
    val es = ElasticSearch(client, User(UUID.randomUUID(), "foobar", "foo@bar.com"))
    try {
      testCode(client, es)
    } finally {
      client.sync.execute {
        deleteIndex(es.uid)
      }
      blocking {
        client.shutdown
      }
    }
  }

  def withContentFixture(testCode: ContentFixture => Any) {
    try testCode(new ContentFixture {})
  }

  override def afterAll() {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  "An indexer" should "create an index entry for a new content item" in withElasticSearch {
    (client, es) => withContentFixture {
      (contentFixture) => {
        val indexer = system.actorOf(Indexer.props(es))
        indexer ! IndexContent(contentFixture.provider, contentFixture.content)

        val ic = expectMsgType[IndexedContent]
        ic.content shouldBe contentFixture.content
        ic.operation shouldBe Operation.Created

        val getResponse = client.sync.execute { get id ic.content.id from es.indexName fields "excerpt"}
        getResponse.isExists shouldBe true
        getResponse.getField("excerpt").getValue shouldBe contentFixture.content.excerpt
      }
    }
  }

  it should "update an index entry for an existing content item" in withElasticSearch {
    (client, es) => withContentFixture {
      (contentFixture) => {
        val indexer = system.actorOf(Indexer.props(es))
        indexer ! IndexContent(contentFixture.provider, contentFixture.content)
        expectMsgType[IndexedContent]

        val changedContent = contentFixture.content.copy(excerpt = "Changed Content")
        indexer ! IndexContent(contentFixture.provider, changedContent)
        val ic = expectMsgType[IndexedContent]
        ic.content shouldBe changedContent
        ic.operation shouldBe Operation.Updated

        val getResponse = client.sync.execute { get id ic.content.id from es.indexName fields "excerpt"}
        getResponse.isExists shouldBe true
        getResponse.getField("excerpt").getValue shouldBe "Changed Content"
      }
    }
  }
}
