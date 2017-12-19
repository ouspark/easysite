
enablePlugins(ScalaJSPlugin)

organization := "com.ouspark"
name := "easysite"
version := "0.1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "com.thoughtworks.binding" %%% "dom" % "11.0.0-M4",
  "com.thoughtworks.binding" %%% "route" % "11.0.0-M4",
  "com.thoughtworks.binding" %%% "futurebinding" % "11.0.0-M4",
  "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
  "io.suzaku" %%% "boopickle" % "1.2.6",
  "com.lihaoyi" %%% "autowire" % "0.2.6",
  "be.doeraene" %%% "scalajs-jquery" % "0.9.2"
)

skip in packageJSDependencies := false
jsDependencies +=
  "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
scalaJSUseMainModuleInitializer := true

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

fork in run := true
javaOptions in run ++= Seq(
  "-Xms256M", "-Xmx2G", "-XX:MaxPermSize=1024M", "-XX:+UseConcMarkSweepGC")

