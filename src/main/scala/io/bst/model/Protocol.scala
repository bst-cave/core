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

  /**
   * Signals a successfully indexed content
   *
   * @param content the content
   * @param provider the provider which created the content
   * @param indexAt a timestamp when the indexing happened
   */
  case class Indexed(content: Content, provider: ContentProvider, indexAt: Instant)

  /**
   * Signals a successful update to an already indexed content
   *
   * @param content the content
   * @param provider the provider which created the content
   * @param updatedAt a timestamp when the update happened
   */
  case class Updated(content: Content, provider: ContentProvider, updatedAt: Instant)
}
