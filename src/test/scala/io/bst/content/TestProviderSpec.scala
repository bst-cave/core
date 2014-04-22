package io.bst.content

import akka.actor.{Props, ActorRef, Actor, ActorSystem}
import akka.testkit.{TestKit, ImplicitSender}
import io.bst.model.Protocol.IndexedContent.Operation
import io.bst.model.Protocol._
import java.time.Instant
import org.scalatest.{FlatSpecLike, BeforeAndAfterAll, Matchers}
import scala.concurrent.duration._


trait ContentFixture {
  val provider = ContentProvider("testProvider", "Test Provider")
  val content = Content("testContent", "test://content", "Test Content")
  val pile = 0 until 10 map (index => Content(s"testContent$index", s"test://content/$index", "Test Content #$index"))
  val indexedContent = IndexedContent(provider, content, Instant.now, Operation.Created)
  val indexedPile = IndexedPile(pile map (IndexedContent(provider, _, Instant.now, Operation.Created)))
}


class TestProvider(workmate: ActorRef) extends Actor with ContentProviderActor with ContentFixture {
  override def receive = {
    case Tick => workmate ! IndexPile(provider, pile)
    case content: Content => workmate ! IndexContent(provider, content)
    case ic: IndexedContent => workmate ! ic
    case ip: IndexedPile => workmate ! ip
  }
}


class TestProviderSpec extends TestKit(ActorSystem("TestProviderSpec"))
                               with ImplicitSender
                               with FlatSpecLike
                               with Matchers
                               with BeforeAndAfterAll {

  override def afterAll() {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  "A content provider" should "index a pile of content items" in new ContentFixture {
    val testProvider = system.actorOf(Props(new TestProvider(testActor)))
    testProvider ! Tick
    expectMsg(IndexPile(provider, pile))
  }

  it should "index a single content item" in new ContentFixture {
    val testProvider = system.actorOf(Props(new TestProvider(testActor)))
    testProvider ! content
    expectMsg(IndexContent(provider, content))
  }

  "A content provider" should "forward a pile of indexed content items" in new ContentFixture {
    val testProvider = system.actorOf(Props(new TestProvider(testActor)))
    testProvider ! indexedPile
    expectMsg(indexedPile)
  }

  it should "forward a single content item" in new ContentFixture {
    val testProvider = system.actorOf(Props(new TestProvider(testActor)))
    testProvider ! indexedContent
    expectMsg(indexedContent)
  }
}
