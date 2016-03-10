name := "akka-registry"

organization := "scalera"

version := "0.1"

scalaVersion := "2.11.7"

fork in Test := true

resolvers += "mvnrepository" at "http://mvnrepository.com/artifact/"

libraryDependencies ++= {
  val akkaVersion = "2.4.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    //  Testing
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    //  Persistence journal
    "org.iq80.leveldb" % "leveldb" % "0.7",
    "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
    //  Logger
    "org.slf4j" % "slf4j-simple" % "1.7.18")
}
