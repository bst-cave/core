package io.bst.content

import akka.actor.{Props, ActorRef, Actor, ActorSystem}
import akka.testkit.{TestKit, ImplicitSender}
import io.bst.model.Protocol._
import org.scalatest.{FlatSpecLike, BeforeAndAfterAll, Matchers}
import scala.concurrent.duration._
import java.time.Instant
import io.bst.model.Protocol.IndexedContent.Operation


object ContentFixture {
  val provider = ContentProvider("dummyProvider", "Dummy Provider")
  val content = Content("dummyContent", "dummy://content", "Dummy Content")
  val pile = 0 until 10 map (index => Content(s"dummyContent$index", s"dummy://content/$index", "Dummy Content #$index"))
  val indexedContent = IndexedContent(provider, content, Instant.now, Operation.Created)
  val indexedPile = IndexedPile(pile map (IndexedContent(provider, _, Instant.now, Operation.Created)))
}


class TestProvider(workmate: ActorRef) extends Actor with ContentProviderActor {
  val provider = ContentFixture.provider

  override def receive = {
    case Tick => workmate ! IndexPile(provider, ContentFixture.pile)
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
  import ContentFixture._

  override def afterAll() {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  "A content provider" should "index a pile of content items" in {
    val testProvider = system.actorOf(Props(new TestProvider(testActor)))
    testProvider ! Tick
    expectMsg(IndexPile(provider, pile))
  }

  it should "index a single content item" in {
    val testProvider = system.actorOf(Props(new TestProvider(testActor)))
    testProvider ! content
    expectMsg(IndexContent(provider, content))
  }

  "A content provider" should "forward a pile of indexed content items" in {
    val testProvider = system.actorOf(Props(new TestProvider(testActor)))
    testProvider ! indexedPile
    expectMsg(indexedPile)
  }

  it should "forward a single content item" in {
    val testProvider = system.actorOf(Props(new TestProvider(testActor)))
    testProvider ! indexedContent
    expectMsg(indexedContent)
  }
}
