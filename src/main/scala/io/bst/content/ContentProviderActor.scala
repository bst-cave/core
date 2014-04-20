package io.bst.content

import akka.actor.{ActorRef, Actor}

/**
 * Trait for actors providing content
 * @author Harald Pehl
 */
trait ContentProviderActor {
  this: Actor =>

  /**
   * A description of this content provider. The content provider's id will be part of the index, so please make
   * sure it's unique.
   */
  def provider: ContentProvider
}
