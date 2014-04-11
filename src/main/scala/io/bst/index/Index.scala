package io.bst.index

import io.bst.contentprovider.Content
import io.bst.user.User
import com.sksamuel.elastic4s.source.DocumentMap

/**
 * An index capable of indexing [[Content]]
 * @author Harald Pehl
 */
trait Index {
  def push(content: Content)
}

class ElasticSearchIndex(user: User) extends Index {

  import com.sksamuel.elastic4s.ElasticClient
  import com.sksamuel.elastic4s.ElasticDsl._

  val client = ElasticClient.local
  client.execute {
    create index user.id.toString
  }


  override def push(content: Content): Unit = client.execute {
    index into user.id.toString doc documentMap(content)
  }

  private def documentMap(content: Content) = {
    new DocumentMap {
      override def map = {
        case Content(url, excerpt, None, Nil) => Map("url" -> url, "excerpt" -> excerpt)
        case Content(url, excerpt, Some(data), Nil) => Map("url" -> url, "excerpt" -> excerpt, "data" -> data)
        case Content(url, excerpt, None, tags) => Map("url" -> url, "excerpt" -> excerpt, "tags" -> tags.mkString(" "))
        case Content(url, excerpt, Some(data), tags) => Map("url" -> url, "excerpt" -> excerpt, "data" -> data, "tags" -> tags.mkString(" "))
      }
    }
  }
}