name := """bst-core"""

version := "0.0.2"

scalaVersion := "2.10.3"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.2",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.2",
  "com.typesafe.akka" %% "akka-quartz-scheduler" % "1.2.0-akka-2.2.x",
  "com.sksamuel.elastic4s" %% "elastic4s" % "1.1.0.0",
  "ch.qos.logback" % "logback-classic" % "1.0.13" % "runtime",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.2" % "test"
)
