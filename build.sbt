
enablePlugins(ScalaJSPlugin)

organization := "com.ouspark"
name := "easysite"
version := "0.1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.3",
  "com.thoughtworks.binding" %%% "dom" % "11.0.0-M4",
  "com.thoughtworks.binding" %%% "route" % "11.0.0-M4",
  "com.thoughtworks.binding" %%% "futurebinding" % "11.0.0-M4",
  "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
  "io.suzaku" %%% "boopickle" % "1.2.6",
  "com.lihaoyi" %%% "autowire" % "0.2.6"
)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
scalaJSUseMainModuleInitializer := true

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

