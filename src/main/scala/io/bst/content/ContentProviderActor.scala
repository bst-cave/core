package io.bst.content

import akka.actor.Actor

/**
 * Trait for actors providing content
 * @author Harald Pehl
 */
trait ContentProviderActor {
  actor: Actor =>

  def provider: ContentProvider
}
