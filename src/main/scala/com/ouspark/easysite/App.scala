package com.ouspark.easysite

import com.ouspark.easysite.routes.SpaceRoute
import com.thoughtworks.binding.Binding.{BindingSeq, Constants}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{Element, Node, document}

import scala.scalajs.js.JSApp

object App extends JSApp {

  override def main(): Unit = {
    dom.render(document.getElementById("layout"), layout)
  }

  @dom
  def layout: Binding[BindingSeq[Node]] = {
    Constants(header, mainDiv).flatMap(_.bind)
  }

  @dom
  def header: Binding[BindingSeq[Node]] = {
    <header>
      <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <a class="navbar-brand" href="#">CP Admin</a>
        <button class="navbar-toggler d-lg-none" type="button" data:data-toggle="collapse" data:data-target="#navbarsExampleDefault" data:aria-controls="navbarsExampleDefault" data:aria-expanded="false" data:aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarsExampleDefault">
          <ul class="navbar-nav mr-auto">
            {
            Constants(SpaceRoute.routes: _*).map { s =>
              val className = if (SpaceRoute.pages.bind.name == s.name) "nav-item active" else "nav-item"
              <li class={className}>
                <a href={s.link} class="nav-link">{s.name.capitalize}</a>
              </li>
            }
            }
          </ul>
          <form class="form-inline mt-2 mt-md-0">
            <input class="form-control mr-sm-2" type="text" placeholder="Search" data:aria-label="Search" />
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
          </form>
        </div>
      </nav>
    </header>
    <!-- -->
  }

  @dom
  def mainDiv: Binding[BindingSeq[Node]] = {
    <div class="container-fluid">
      { SpaceRoute.pages.bind.render.bind }
    </div>
    <!-- -->
  }

}
