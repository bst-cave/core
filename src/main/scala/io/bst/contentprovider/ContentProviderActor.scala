package io.bst.contentprovider

import akka.actor.Actor
import io.bst.contentprovider.ContentProviderActor.WhoAreYou
import io.bst.user.User


/**
 * Protocol for content providers
 */
object ContentProviderActor {

  /**
   * The content provided by a content provider.
   * @param id an unique id for this content
   * @param url the url for the content
   * @param excerpt an abstract over the content
   * @param data an optional string with more details
   * @param tags an optional list of tags
   */
  case class Content(id: String, url: String, excerpt: String, data: Option[String] = None, tags: Seq[String] = Nil)


  /**
   * Poll all content items
   */
  case class Pull(user: User)

  /**
   * Push a single content item
   */
  case class Push(user: User, content: Content)

  case class WhoAreYou()

  /**
   * Metadata for a content provider
   * @param id An unique ID. Must be unique across all content providers
   * @param name the name
   * @param description an optional description
   */
  case class ContentProviderInfo(id: String, name: String, description: Option[String] = None)

}


/**
 * Trait for content provider actors
 */
trait ContentProviderActor {
  this: Actor =>

  import ContentProviderActor.ContentProviderInfo
  
  /**
   * A description of this content provider. The content provider's id will be part of the index and must be
   * unique across all content providers.
   */
  def info: ContentProviderInfo

  def whoAreYou: Receive = {
    case WhoAreYou => sender() ! info
  }
}
