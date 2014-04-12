package io.bst.content

import akka.actor.{Props, ActorRef, Actor}
import scala.io.Source
import io.bst.model.Protocol.{Index, Tick}


object TextProvider {
  /**
   * Create Props for an actor of this type.
   * @param indexer The indexer actor.
   * @return a Props for creating this actor, which can then be further configured
   *         (e.g. calling `.withDispatcher()` on it)
   */
  def props(indexer: ActorRef): Props = Props(new TextProvider("random.txt", indexer))
}


/**
 * An actor which provides content from a text file. 
 * @author Harald Pehl
 */
class TextProvider(textFile: String, indexer: ActorRef) extends Actor {

  val provider = ContentProvider(getClass.getName, "Random Text")

  override def receive = {

    case Tick => {
      val lines = for {
        (line, idx) <- Source.fromInputStream(getClass.getResourceAsStream(textFile)).getLines().zipWithIndex if !line.stripLineEnd.isEmpty
      } yield Content(s"file://random.txt#$idx", line)

      lines.foreach(content => indexer ! Index(content, provider))
    }
  }
}
