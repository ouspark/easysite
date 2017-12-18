package com.ouspark.easysite

import com.ouspark.easysite.routes.SpaceRoute
import com.thoughtworks.binding.Binding.BindingSeq
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.{Node, document, window}

import scala.scalajs.js
import scala.scalajs.js.JSApp


object App extends JSApp {

  val $ = js.Dynamic.global.$
  override def main(): Unit = {
    dom.render(document.getElementById("container"), layout)
    installSettings()
    installEvents()
  }

  @dom
  def layout: Binding[BindingSeq[Node]] = {
    SpaceRoute.pages.bind.render.bind
  }

  def installSettings() = Binding {
    val h = SpaceRoute.pages.bind
    $("#nav-accordion").dcAccordion(js.Dynamic.literal(
      "eventType" -> "click", "autoClose" -> true
    , "saveState" -> true, "disableLink" -> true
    , "speed" -> "slow", "showCount" -> false
    , "autoExpand" -> true, "classExpand" -> "dcjq-current-parent"))

    $("#sidebar .sub-menu > a").click(() => {
      val o = $("#sidebar .sub-menu > a").offset()
      val diff = 255 - o.top.asInstanceOf[Int]
      if(diff > 0) $("#sidebar").scrollTo("-=" + Math.abs(diff), 500)
      else $("#sidebar").scrollTo("+=" + Math.abs(diff), 500)
    })

    def responsiveView() = {
      val wSize = $(window).width.asInstanceOf[Int]
      if(wSize <= 768) {
        $("#container").addClass("sidebar-close")
        $("#sidebar > ul").hide()
      } else {
        $("#container").removeClass("sidebar-close")
        $("#sidebar > ul").show()
      }
    }
    $(window).on("load", responsiveView)
    $(window).on("resize", responsiveView)

//    $(".fa-bars").click(() => {
//      if($("#sidebar > ul").is(":visible").asInstanceOf[Boolean]) {
//        $("#main-content").css(js.Dynamic.literal("margin-left" -> "0px"))
//        $("#sidebar").css(js.Dynamic.literal("margin-left" -> "-210px"))
//        $("#sidebar > ul").hide()
//        $("#container").addClass("sidebar-closed")
//      } else {
//        $("#main-content").css(js.Dynamic.literal("margin-left" -> "210px"))
//        $("#sidebar > ul").show()
//        $("#sidebar").css(js.Dynamic.literal("margin-left" -> "0px"))
//        $("#container").removeClass("sidebar-closed")
//      }
//    })

    $("#sidebar").niceScroll(js.Dynamic.literal(
      "styler" -> "fb", "cursorcolor" -> "#e8403f", "cursorwidth" -> "3", "cursorborderradius" -> "10px"
      , "background" -> "#404040", "spacebarenabled" -> false, "cursorborder" -> "", "scrollspeed" -> 60
    ))
    $(".table-responsive").niceScroll(js.Dynamic.literal(
      "styler" -> "fb", "cursorcolor" -> "#e8403f", "cursorwidth" -> "6", "cursorborderradius" -> "10px"
      , "background" -> "#404040", "spacebarenabled" -> false, "cursorborder" -> "", "zindex" -> "1000", "horizrailenabled" -> true
    ))
  }
  def installEvents() = Binding {
    SpaceRoute.pages.bind.install()
  }

}


