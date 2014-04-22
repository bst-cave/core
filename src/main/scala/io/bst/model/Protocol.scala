package io.bst.model

import io.bst.content.Content
import io.bst.content.ContentProvider
import io.bst.model.Protocol.IndexedContent.Operation.Operation
import java.time.Instant

/**
 * @author Harald Pehl
 */
object Protocol {

  /**
   * Triggers a content provider to provide content
   */
  case class Tick()

  /**
   * Command message to index content from a provider
   *
   * @param provider the provider which created the content
   * @param content the content
   */
  case class IndexContent(provider: ContentProvider, content: Content)

  /**
   * Command message to index a pile of content from a provider
   *
   * @param provider the provider which created the content
   * @param pile the content
   */
  case class IndexPile(provider: ContentProvider, pile: Seq[Content])

  object IndexedContent {

    object Operation extends Enumeration {
      type Operation = Value
      val Created = Value("created")
      val Updated = Value("updated")
      val Removed = Value("removed")
      val Undefined = Value("undefined")
    }

  }

  /**
   * Signals a successfully indexed content
   *
   * @param provider the provider which created the content
   * @param content the content
   * @param timestamp a timestamp when the indexing happened
   * @param operation whether the content was indexed for the first time or updated
   */
  case class IndexedContent(provider: ContentProvider, content: Content, timestamp: Instant, operation: Operation)

  /**
   * To make pattern matching more type safe
   * @param pile the indexed content items
   */
  case class IndexedPile(pile: Seq[IndexedContent])
}
