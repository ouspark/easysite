package com.ouspark.easysite
package routes

import com.ouspark.easysite.pages.{CPPublisher, CPRecordType, Home, Service}
import com.thoughtworks.binding.{Binding, Route, dom}
import com.thoughtworks.binding.Binding.{BindingSeq, Var}
import org.scalajs.dom.raw.Node

trait Loc {
  def name: String
  def link: String = s"#${name}"
}

trait Space extends Loc {
  def render: Binding[BindingSeq[Node]] = Space.defaultRender
  def css: Option[BindingSeq[Node]] = None
  def install(): Unit = {}
}

object Space {
  @dom
  def defaultRender: Binding[BindingSeq[Node]] = {
    <div class="header">
      <h1>{SpaceRoute.pages.bind.name.capitalize +  " Page Title"}</h1>
      <h2>{"A subtitle for " + SpaceRoute.pages.bind.name.capitalize + " goes here"}</h2>
    </div>
    <!-- -->
  }
}

object SpaceRoute {


  val home = Home

  val routes = List(home, new CPPublisher(None, None, None), Service, CPRecordType)

  val route = Route.Hash[Space](home)(
    new Route.Format[Space] {
      override def unapply(hashText: String): Option[Space] = hashText match { //Some(routes.find(_.name == hashText.drop(1)).getOrElse(home))
        case "#publisher" => Some(new CPPublisher(None, None, None))
        case hash if(hash.split("/").length == 4 && hash.split("/")(0) == "#publisher") =>
          Some(new CPPublisher(Some(hash.split("/")(1)), Some(hash.split("/")(2)), Some(hash.split("/")(3))))
        case "#cap" => Some(CPRecordType)
        case "#service" => Some(Service)
        case _ => Some(home)
      }
      override def apply(state: Space): String = state.name
    }
  )

  route.watch()

  val pages: Var[Space] = route.state

}
