package io.bst.contentprovider

import java.net.URL

/**
 * The content provided by a content provider.
 * @param url the url for the content
 * @param excerpt an abstract over the content
 * @param data an optional string with more details
 * @param tags an optional list of tags
 *
 * @author Harald Pehl
 */
case class Content(url: URL, excerpt: String, data: Option[String] = None, tags: Seq[String] = Nil)

/**
 * Content providers are responsible for collecting content from services which provide some kind of bookmarks.
 * Examples are Google+ posts or starred tweets.
 *
 * @author Harald Pehl
 */
trait ContentProvider {
  def content: Content
}
