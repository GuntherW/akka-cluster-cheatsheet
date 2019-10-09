import Dependencies._

name := "akka-cluster-cheatsheet"
organization := "de.codecentric.wittig"
version := "1.0"
scalaVersion := "2.12.10"
scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-encoding",
  "UTF-8",
  "-Xlint",
  "-Xverify",
  "-feature",
  "-language:postfixOps"
)

libraryDependencies ++= Seq(
  "ch.qos.logback"    % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-actor"     % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j"     % akkaVersion,
  // "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-cluster"          % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib"          % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools"    % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
  "com.typesafe.akka" %% "akka-distributed-data" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence"      % akkaVersion,
  "com.twitter"       %% "chill-akka"            % "0.9.3",
  "org.json4s"        %% "json4s-jackson"        % "3.6.7",
  "com.typesafe"      % "config"                 % "1.3.4",
  "com.typesafe.akka" %% "akka-testkit"          % akkaVersion % "test"
)

maintainer := "Gunther Wittig <gunther.wittig@codecentric.de>"
dockerRepository := Some("guntherw")
dockerBaseImage := "java"
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
