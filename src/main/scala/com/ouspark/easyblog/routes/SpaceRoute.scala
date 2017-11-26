package com.ouspark.easyblog
package routes

import com.ouspark.easyblog.pages.{About, Post, Home, Publisher}
import com.thoughtworks.binding.{Binding, Route, dom}
import com.thoughtworks.binding.Binding.{BindingSeq, Var}
import org.scalajs.dom.raw.Node

trait Loc {
  def name: String
  def link: String = s"#${name}"
}

trait Space extends Loc {
  def render: Binding[BindingSeq[Node]] = Space.defaultRender
  def css: Option[Binding[BindingSeq[Node]]] = None
  def install(): Unit = {}
}

object Space {
  @dom
  def defaultRender: Binding[BindingSeq[Node]] = {
    <h1>{SpaceRoute.pages.bind.name.capitalize +  " Page Title"}</h1>
    <h2>{"A subtitle for " + SpaceRoute.pages.bind.name.capitalize + " goes here"}</h2>
    <!-- -->
  }
}

object SpaceRoute {

  val home = Home

  val route = Route.Hash[Space](home)(
    new Route.Format[Space] {
      override def unapply(hashText: String): Option[Space] = hashText.drop(1) match {
        case "publisher" => Some(Publisher)
        case s if (s.startsWith("post/")) => Some(Post(s))
        case _ => Some(home)
      }
      override def apply(state: Space): String = state.name
    }
  )

  route.watch()

  val pages: Var[Space] = route.state

}
