package com.ouspark.easyblog

import com.ouspark.easyblog.pages.{Home, Publisher}
import com.ouspark.easyblog.routes.SpaceRoute
import com.thoughtworks.binding.Binding.{BindingSeq, Constants}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{Element, Node, document}

import scala.scalajs.js
import scala.scalajs.js.JSApp

object App extends JSApp {

  val $ = js.Dynamic.global.$
  override def main(): Unit = {
    dom.render(document.getElementById("layout"), layout)
    make.watch()
  }

  @dom
  def layout: Binding[BindingSeq[Node]] = {
    Constants(sidebar, content).flatMap(_.bind)
  }

  @dom
  def sidebar: Binding[BindingSeq[Node]] = {
    <div class="sidebar pure-u-1 pure-u-md-1-4">
      <div class="header">
        <h1 class="brand-title">Ouspark's Blog</h1>
        <h2 class="brand-tagline">Creating a blog layout using Pure</h2>

        <nav class="nav">
          <ul class="nav-list">
            <li class="nav-item">
              <a class="pure-button" href={Home.link}>Pure</a>
            </li>
            <li class="nav-item">
              <a class="pure-button" href={Publisher.link}>YUI Library</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
    <!-- -->
  }

  @dom
  def content: Binding[BindingSeq[Node]] = {

    <div class="content pure-u-1 pure-u-md-3-4">
      { SpaceRoute.pages.bind.render.bind }
    </div>
    <!-- -->
  }

  @dom
  def make = {
    val h = SpaceRoute.pages.bind
    $("textarea").froalaEditor();
  }
}
