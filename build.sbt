name := """bst-core"""

version := "0.0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.2",
  "com.sksamuel.elastic4s" %% "elastic4s" % "1.1.0.0",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test"
)
