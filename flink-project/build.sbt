ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal
)

name := "flink-project"

version := "0.1-SNAPSHOT"

organization := "org.dhacky"

ThisBuild / scalaVersion := "2.11.12"

val flinkVersion = "1.7.2"

// Maven/Ivy dependencies
// groupId % artifactId % version [% configuration]
// %% add scala version to artifactId
val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-jdbc" % flinkVersion)

val dependencies = Seq(
  "ru.yandex.clickhouse" % "clickhouse-jdbc" % "0.1.50")

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies ++ dependencies
  )

assembly / mainClass := Some("org.dhacky.Job")

// make run command include the provided dependencies
Compile / run := Defaults.runTask(Compile / fullClasspath,
  Compile / run / mainClass,
  Compile / run / runner
).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false)
