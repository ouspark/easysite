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
    Constants(menu, mainDiv).flatMap(_.bind)
  }

  def toggleClass(ele: Element, className: String) = {
    if (ele.classList.contains(className)) {
      ele.classList.remove(className)
    } else {
      ele.classList.add(className)
    }
  }

  def toggleAll(e: Event) = {
    val active = "active"
    e.preventDefault()
    toggleClass(document.getElementById("layout"), active)
    toggleClass(document.getElementById("menu"), active)
    toggleClass(document.getElementById("menuLink"), active)
  }

  @dom
  def menu: Binding[BindingSeq[Node]] = {
    <a href="#menu" id="menuLink" class="menu-link" onclick={event: Event => toggleAll(event)}>
      <!-- Hamburger icon -->
      <span></span>
    </a>
    <div id="menu">
      <div class="pure-menu">
        <a class="pure-menu-heading" href="#">Company</a>

        <ul class="pure-menu-list">
          {
            Constants(SpaceRoute.routes: _*).map { s =>
              val className = if (SpaceRoute.pages.bind.name == s.name) "pure-menu-item menu-item-divided pure-menu-selected" else "pure-menu-item"
              <li class={className}>
                <a href={s.link} class="pure-menu-link">{s.name.capitalize}</a>
              </li>
            }
          }
        </ul>
      </div>
    </div>
  }

  @dom
  def mainDiv: Binding[BindingSeq[Node]] = {
    <div id="main" onclick={ event: Event => if (document.getElementById("menu").classList.contains("active")) toggleAll(event) }>
      { SpaceRoute.pages.bind.render.bind }
    </div>
    <!-- -->
  }
}
