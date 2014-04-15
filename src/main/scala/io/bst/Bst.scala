package io.bst

import akka.actor.ActorSystem
import io.bst.content.FileProvider
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import io.bst.user.User
import io.bst.model.Protocol.Tick
import com.sksamuel.elastic4s.ElasticClient
import io.bst.ext.ElasticSearch
import java.io.File

/**
 * @author Harald Pehl
 */
object Bst extends App {

  val system = ActorSystem("bst")
  val es = ElasticSearch(ElasticClient.local, User.testUser)

  val filename = new File(".").getCanonicalPath + "/random.txt"
  val fileProvider = system.actorOf(FileProvider.props(filename, es), "fileProvider")
  QuartzSchedulerExtension(system).schedule("Every10Seconds", fileProvider, Tick)
}
