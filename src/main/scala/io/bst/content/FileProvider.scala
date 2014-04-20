package io.bst.content

import akka.actor.{ActorLogging, Props, Actor}
import io.bst.ext.ElasticSearch
import io.bst.index.Indexer
import io.bst.model.Protocol._
import io.bst.stats.Stats
import java.security.MessageDigest
import scala.io.Source


object FileProvider {
  /**
   * Create Props for an actor of this type.
   * @param es The ElasticSearch singleton.
   * @return a Props for creating this actor, which can then be further configured
   *         (e.g. calling `.withDispatcher()` on it)
   */
  def props(filename: String, es: ElasticSearch): Props = Props(new FileProvider(filename, es))
}


/**
 * An actor which provides content from a text file. 
 * @author Harald Pehl
 */
class FileProvider(filename: String, es: ElasticSearch) extends Actor with ActorLogging with ContentProviderActor {

  val provider = ContentProvider(getClass.getName, "File Provider")
  val indexer = context.actorOf(Indexer.props(es), "indexer")
  val stats = context.actorOf(Props[Stats], "stats")

  override def receive = {

    case Tick =>
      log.debug("Provide all content from {}", filename)

      val pile = for {
        (line, idx) <- Source.fromFile(filename).getLines().zipWithIndex if !line.stripLineEnd.isEmpty
        id = md5(line)
      } yield Content(id, s"file://$filename#$idx", line)

      indexer ! IndexPile(provider, pile.toSeq)

    case content: Content => indexer ! IndexContent(provider, content)

    case ic: IndexedContent => stats ! ic

    case ip: IndexedPile => stats ! ip
  }

  private def md5(s: String) = {
    val bytes = MessageDigest.getInstance("MD5").digest(s.getBytes)
    bytes.map(byte => Integer.toHexString(0xFF & byte)).mkString
  }
}
