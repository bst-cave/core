package io.bst.content

/**
 * Metadata for a content provider
 * @param id An unique ID
 * @param name the name
 * @param description an optional description
 */
case class ContentProvider(id: String, name: String, description: Option[String] = None)
