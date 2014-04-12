package io.bst

import akka.actor.{Props, ActorSystem}
import io.bst.content.TextProvider
import io.bst.model.Protocol.Tick
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import io.bst.index.Indexer

/**
 * @author Harald Pehl
 */
object Bst extends App {

  val system = ActorSystem("bst")
  val indexer = system.actorOf(Props[Indexer], "indexer")
  val textProvider = system.actorOf(TextProvider.props(indexer), "textProvider")
  QuartzSchedulerExtension(system).schedule("Every30Seconds", textProvider, Tick)

}
