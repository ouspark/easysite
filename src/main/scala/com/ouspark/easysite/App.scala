package com.ouspark.easysite

import com.ouspark.easysite.routes.SpaceRoute
import com.thoughtworks.binding.Binding.{BindingSeq, Constants}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{Element, Node, document}

import scala.scalajs.js.JSApp

object App extends JSApp {

  override def main(): Unit = {
    dom.render(document.getElementById("container"), layout)
  }

  @dom
  def layout: Binding[BindingSeq[Node]] = {
    SpaceRoute.pages.bind.render.bind
  }

}
