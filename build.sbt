name := """bst-core"""

version := "0.0.3"

scalaVersion := "2.10.3"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Spray" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaVersion = "2.3.2"
  val sprayStandaloneVersion = "1.3.1"
  val sprayScalaVersion = "1.2.6"
  Seq(
    "io.spray"               %% "spray-json"            % sprayScalaVersion,
    "io.spray"               %  "spray-routing"         % sprayStandaloneVersion,
    "io.spray"               %  "spray-caching"         % sprayStandaloneVersion,
    "io.spray"               %  "spray-can"             % sprayStandaloneVersion,
    "io.spray"               %  "spray-client"          % sprayStandaloneVersion,
    "io.spray"               %  "spray-testkit"         % sprayStandaloneVersion % "test",
    "com.typesafe.akka"      %% "akka-actor"            % akkaVersion,
    "com.typesafe.akka"      %% "akka-slf4j"            % akkaVersion,
    "com.typesafe.akka"      %% "akka-quartz-scheduler" % "1.2.0-akka-2.2.x",
    "com.typesafe.akka"      %% "akka-testkit"          % akkaVersion            % "test",
    "com.sksamuel.elastic4s" %% "elastic4s"             % "1.1.0.0",
    "ch.qos.logback"         %  "logback-classic"       % "1.0.13"               % "runtime",
    "org.scalatest"          %% "scalatest"             % "2.1.3"                % "test"
  )
}

Revolver.settings
