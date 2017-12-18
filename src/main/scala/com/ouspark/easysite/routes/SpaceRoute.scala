package com.ouspark.easysite
package routes

import com.ouspark.easysite.pages.{CPPublisher, CPRecordType, Home, Service}
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Var}
import com.thoughtworks.binding.{Binding, Route, dom}
import org.scalajs.dom.Event
import org.scalajs.dom.raw.Node

import scala.scalajs.js


trait Loc {
  def name: String
  def link: String = s"#${name}"
}

trait Space extends Loc {
  def header: Binding[Node] = Space.header
  def sidebar: Binding[Node] = Space.sidebar
  def content: Binding[Node] = Space.content
  def footer: Binding[Node] = Space.footer
  @dom
  def render: Binding[BindingSeq[Node]] = {
    Constants(header, sidebar, content, footer).map(_.bind)
  }
  def css: Option[BindingSeq[Node]] = None
  def install(): Unit = {}
}

object Space {

  @dom def header: Binding[Node] = {
    def clickToggle() = {event: Event =>
      val $ = js.Dynamic.global.$
      if($("#sidebar > ul").is(":visible").asInstanceOf[Boolean]) {
        $("#main-content").css(js.Dynamic.literal("margin-left" -> "0px"))
        $("#sidebar").css(js.Dynamic.literal("margin-left" -> "-210px"))
        $("#sidebar > ul").hide()
        $("#container").addClass("sidebar-closed")
      } else {
        $("#main-content").css(js.Dynamic.literal("margin-left" -> "210px"))
        $("#sidebar > ul").show()
        $("#sidebar").css(js.Dynamic.literal("margin-left" -> "0px"))
        $("#container").removeClass("sidebar-closed")
      }
    }
    <header class="header white-bg">
      <div class="sidebar-toggle-box">
        <div data:data-original-title="Toggle Navigation" data:data-placement="right" class="fa fa-bars tooltips" onclick={ clickToggle() }></div>
      </div>
      <!--logo start-->
      <a href={ SpaceRoute.home.link } class="logo" >CP<span> admin</span></a>
      <!--logo end-->
    </header>
  }

  @dom def sidebar: Binding[Node] = {
    <aside>
      <div id="sidebar"  class="nav-collapse ">
        <ul class="sidebar-menu" id="nav-accordion">
          {
          Constants(SpaceRoute.routes: _*).map { s =>
            val className = if (SpaceRoute.pages.bind.name == s.name) "active" else ""
            <li>
              <a class={className} href={s.link}>
                <i class="fa fa-dashboard"></i>
                <span>{s.name.capitalize}</span>
              </a>
            </li>
          }
          }
        </ul>
      </div>
    </aside>
  }

  @dom def content: Binding[Node] = {
    <section id="main-content">
      <section class="wrapper">
        <div class="row">
          <div class="col-lg-12">
            <!--breadcrumbs start -->
            <ul class="breadcrumb">
              <li><a href={ SpaceRoute.pages.bind.link }><i class="fa fa-home"></i>{ SpaceRoute.pages.bind.name.capitalize }</a></li>
              <li class="active">{"This is " + SpaceRoute.pages.bind.name.capitalize + " page"}</li>
            </ul>
            <!--breadcrumbs end -->
          </div>
        </div>
      </section>
    </section>
  }

  @dom def footer: Binding[Node] = {
    <footer class="site-footer">
      <div class="text-center">
        2017 &copy; CPAdmin by Ouspark.
        <a href="#" class="go-top">
          <i class="fa fa-angle-up"></i>
        </a>
      </div>
    </footer>
  }
}

object SpaceRoute {


  val home = Home

  val routes = List(home, CPRecordType, new CPPublisher(None, None, None), Service)

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
