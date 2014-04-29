package io.bst.elasticsearch

import com.sksamuel.elastic4s.ElasticClient


trait ElasticClientComponent {
  def elasticClient: ElasticClient
}
