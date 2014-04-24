package io.bst

import akka.actor.{Props, Actor, ActorSystem}
import com.sksamuel.elastic4s.ElasticClient
import io.bst.ext.ElasticSearch
import io.bst.user.User
import java.util.UUID
import java.util.UUID.randomUUID
import akka.io.IO
import spray.can.Http


object Boot extends App {
  implicit val system = ActorSystem("bst-core")
  val api = system.actorOf(Props[Api], "api")
  IO(Http) ! Http.Bind(api, interface = "localhost", port = 8080)

//  val es = ElasticSearch(ElasticClient.local, User(randomUUID, "foobar", "foo@bar.com"))
//  val filename = new File(".").getCanonicalPath + "/random.txt"
//  val fileProvider = system.actorOf(FileProvider.props(filename, es), "fileProvider")
//  QuartzSchedulerExtension(system).schedule("Every10Seconds", fileProvider, Tick)
}
