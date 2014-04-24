name := """bst-core"""

version := "0.0.2"

scalaVersion := "2.10.3"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Spray" at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.2",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.2",
  "com.typesafe.akka" %% "akka-quartz-scheduler" % "1.2.0-akka-2.2.x",
  "io.spray" %%  "spray-json" % "1.2.6",
  "com.sksamuel.elastic4s" %% "elastic4s" % "1.1.0.0",
  "com.google.api-client" % "google-api-client-java6" % "1.18.0-rc",
  "com.google.http-client" % "google-http-client-jackson2" % "1.18.0-rc",
  "ch.qos.logback" % "logback-classic" % "1.0.13" % "runtime",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.2" % "test"
)
