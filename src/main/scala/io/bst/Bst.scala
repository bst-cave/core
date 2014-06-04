package io.bst

import com.sksamuel.elastic4s.ElasticClient
import io.bst.api.Api
import io.bst.core.{CoreActors, BootedCore}
import io.bst.elasticsearch.ElasticClientComponent
import io.bst.web.Web

object Bst extends App with BootedCore with CoreActors with ElasticClientComponent with Api with Web {
  override val elasticClient = ElasticClient.local
}
