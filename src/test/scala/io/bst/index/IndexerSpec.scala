package io.bst.index

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import io.bst.content.ContentFixture._
import io.bst.ext.ElasticSearch
import io.bst.model.Protocol.IndexedContent.Operation
import io.bst.model.Protocol.{IndexedContent, IndexContent}
import io.bst.user.User
import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpecLike}
import scala.concurrent.duration._


class IndexerSpec extends TestKit(ActorSystem("IndexerSpec"))
                          with ImplicitSender
                          with FlatSpecLike
                          with Matchers
                          with BeforeAndAfterAll {

  var client: ElasticClient = _
  var es: ElasticSearch = _

  override def beforeAll() {
    client = ElasticClient.local
    es = ElasticSearch(client, User.testUser)
  }

  override def afterAll() {
    client.sync.execute { deleteIndex(es.uid) }
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  "An indexer" should "create an index entry for new context" in {
    val indexer = system.actorOf(Indexer.props(es))
    indexer ! IndexContent(provider, content)
    val ic = expectMsgType[IndexedContent]
    ic.content shouldBe content
    ic.operation shouldBe Operation.Created
  }
}
