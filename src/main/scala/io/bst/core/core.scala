package io.bst.core

import akka.actor.{Props, ActorSystem}
import io.bst.contentprovider.ContentProvidersActor
import io.bst.elasticsearch.{ElasticSearch, ElasticClientComponent}
import io.bst.index.IndexActor
import io.bst.stats.StatsActor

/**
 * Core is type containing the ``system: ActorSystem`` member. This enables us to use it in our
 * apps as well as in our tests.
 */
trait Core {

  implicit def system: ActorSystem

}


/**
 * This trait implements ``Core`` by starting the required ``ActorSystem`` and registering the
 * termination handler to stop the system when the JVM exits.
 */
trait BootedCore extends Core {

  /**
   * Construct the ActorSystem we will use in our application
   */
  implicit lazy val system = ActorSystem("akka-spray")

  /**
   * Ensure that the constructed ActorSystem is shut down when the JVM shuts down
   */
  sys.addShutdownHook(system.shutdown())
}


/**
 * This trait contains the actors that make up our application; it can be mixed in with
 * ``BootedCore`` for running code or ``TestKit`` for unit and integration tests.
 *
 * In order to create the top level actors, dependencies to external services like
 * elastic search need to be mixed in.
 */
trait CoreActors {
  this: Core with ElasticClientComponent =>

  val stats = system.actorOf(Props[StatsActor], "stats")
  val index = system.actorOf(IndexActor.props(new ElasticSearch(elasticClient)))
  val contentProviders = system.actorOf(ContentProvidersActor.props(index))
}