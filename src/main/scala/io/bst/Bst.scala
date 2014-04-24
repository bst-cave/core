package io.bst

import akka.actor.ActorSystem
import com.sksamuel.elastic4s.ElasticClient
import io.bst.ext.ElasticSearch
import io.bst.user.User
import java.util.UUID


/**
 * @author Harald Pehl
 */
object Bst extends App {
  val system = ActorSystem("bst")
  val es = ElasticSearch(ElasticClient.local, User(UUID.randomUUID(), "foobar", "foo@bar.com"))

//  val filename = new File(".").getCanonicalPath + "/random.txt"
//  val fileProvider = system.actorOf(FileProvider.props(filename, es), "fileProvider")
//  QuartzSchedulerExtension(system).schedule("Every10Seconds", fileProvider, Tick)
}
