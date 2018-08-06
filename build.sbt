import Dependencies._


name := "akkaclustersample"
organization := "de.codecentric.wittig"
version := "1.0"
scalaVersion := "2.12.6"
scalacOptions ++= Seq(
  "-deprecation"
  ,"-unchecked"
  ,"-encoding", "UTF-8"
  ,"-Xlint"
  ,"-Xverify"
  ,"-feature"
  ,"-language:postfixOps"
)


libraryDependencies ++= Seq (
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
  "com.typesafe.akka" %% "akka-distributed-data" % akkaVersion,
  "com.twitter" %% "chill-akka" % "0.9.2",

  "org.json4s" % "json4s-jackson_2.12" % "3.6.0",
  "com.typesafe" % "config" % "1.3.3"
)

maintainer := "Gunther Wittig <gunther.wittig@codecentric.de>"
dockerRepository := Some("guntherw")
dockerBaseImage := "java"
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)