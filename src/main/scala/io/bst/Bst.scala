package io.bst

import akka.actor.ActorSystem
import io.bst.content.TextProvider
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import io.bst.user.User
import io.bst.model.Protocol.Tick
import com.sksamuel.elastic4s.ElasticClient
import io.bst.ext.ElasticSearch

/**
 * @author Harald Pehl
 */
object Bst extends App {

  val system = ActorSystem("bst")
  val es = ElasticSearch(ElasticClient.local, User.testUser)

  val textProvider = system.actorOf(TextProvider.props(es), "textProvider")
  QuartzSchedulerExtension(system).schedule("Every30Seconds", textProvider, Tick)
}
