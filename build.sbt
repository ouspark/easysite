import org.scalajs.sbtplugin.ScalaJSPlugin.AutoImport.jsDependencies
import sbt.Keys.name

import scala.languageFeature.reflectiveCalls


val akkaVersion = "2.5.8"
val akkaHttpVersion = "10.1.0-RC1"

val app = crossProject.settings(
  organization := "com.ouspark",
  name := "easysite",
  version := "0.1.0",
  unmanagedSourceDirectories in Compile += baseDirectory.value / "shared" / "main" / "scala",
  unmanagedResourceDirectories in Compile += baseDirectory.value / ".." / "static",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "upickle" % "0.5.1",
    "joda-time" % "joda-time" % "2.9.9",
    "org.joda" % "joda-convert" % "1.8.1",
    "org.scalatest" %%% "scalatest" % "3.0.1" % "test"
  ),
  scalaVersion := "2.12.3",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
).jsSettings(
  libraryDependencies ++= Seq(
    "com.thoughtworks.binding" %%% "dom" % "11.0.0-M4",
    "com.thoughtworks.binding" %%% "binding" % "11.0.0-M4",
    "com.thoughtworks.binding" %%% "route" % "11.0.0-M4",
    "com.thoughtworks.binding" %%% "futurebinding" % "11.0.0-M4"
  ),
  skip in packageJSDependencies := false,
  jsDependencies +=
    "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js",

  scalaJSUseMainModuleInitializer := true,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

).jvmSettings(
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "ch.qos.logback"    %  "logback-classic" % "1.1.3",
    "com.typesafe.akka" %% "akka-testkit"    % akkaVersion   % "test"
  )
)

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

lazy val appJS = app.js.enablePlugins(ScalaJSPlugin).settings(
  artifactPath in(Compile, fastOptJS) := baseDirectory.value / ".." / "jvm" / "src" / "main" / "resources" / "cpadmin" / "js" / "fastOpt.js"
  )
lazy val appJVM = app.jvm.settings(
  (resources in Compile) += (fastOptJS in (appJS, Compile)).value.data
)
  .settings(sharedSettings: _*)
  .settings()

lazy val sharedSettings = Seq(
  // File changes in `/static` should never trigger new compilation
  watchSources := watchSources.value.filterNot(_.getPath.contains("static")).filterNot(_.getPath.contains("webapp"))
)

//fork in run := true
//javaOptions in run ++= Seq(
//  "-Xms256M", "-Xmx2G", "-XX:MaxPermSize=1024M", "-XX:+UseConcMarkSweepGC")

