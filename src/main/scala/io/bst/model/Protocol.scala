package io.bst.model

import java.time.Instant
import io.bst.content.Content
import io.bst.content.ContentProvider

/**
 * @author Harald Pehl
 */
object Protocol {

  /**
   * Triggers a content provider to provide content
   */
  case class Tick()

  /**
   * Command message for an indexer to index content from a provider
   * @param content the content
   * @param provider the provider which created the content
   */
  case class Index(content: Content, provider: ContentProvider)

  /**
   * Signals a successfully indexed content
   * @param content the content
   * @param provider the provider which created the content
   * @param indexAt a timestamp when the indexing happened
   */
  case class Indexed(content: Content, provider: ContentProvider, indexAt: Instant)
}
