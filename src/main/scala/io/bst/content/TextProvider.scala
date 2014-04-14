package io.bst.content

import akka.actor.{Props, Actor}
import scala.io.Source
import io.bst.model.Protocol._
import java.util.Base64
import io.bst.index.Indexer
import io.bst.model.Protocol.Tick
import io.bst.model.Protocol.Indexed
import io.bst.model.Protocol.Created
import io.bst.ext.ElasticSearch


object TextProvider {
  /**
   * Create Props for an actor of this type.
   * @param es The ElasticSearch singleton.
   * @return a Props for creating this actor, which can then be further configured
   *         (e.g. calling `.withDispatcher()` on it)
   */
  def props(es: ElasticSearch): Props = Props(new TextProvider("random.txt", es))
}


/**
 * An actor which provides content from a text file. 
 * @author Harald Pehl
 */
class TextProvider(textFile: String, es: ElasticSearch) extends Actor with ContentProviderActor {

  val indexer = context.actorOf(Indexer.props(es))
  // TODO statistics

  override def provider = ContentProvider(getClass.getName, "Text Provider")

  override def receive = {

    case Tick =>
      val pile = for {
        (line, idx) <- Source.fromInputStream(getClass.getResourceAsStream(textFile)).getLines().zipWithIndex if !line.stripLineEnd.isEmpty
        id = Base64.getEncoder.encodeToString(line.getBytes)
      } yield Content(id, s"file://random.txt#$idx", line)

      indexer ! IndexPile(provider, pile.toSeq)

    case Created(content, provider, timestamp) =>

    case Indexed(content, provider, timestamp) =>
  }
}
