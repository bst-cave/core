package io.bst.content

/**
 * The content provided by a content provider.
 * @param url the url for the content
 * @param excerpt an abstract over the content
 * @param data an optional string with more details
 * @param tags an optional list of tags
 */
case class Content(url: String, excerpt: String, data: Option[String] = None, tags: Seq[String] = Nil)
