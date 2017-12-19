package com.ouspark.easysite
package pages

import com.ouspark.easysite.models._
import com.ouspark.easysite.routes.Space
import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{HTMLElement, Node}

import scala.scalajs.js

object Home extends Space {

  override def name: String = "home"

  val tasks = List(Todo("Flatlab is Modern Dashboard", Critical, Some("2 Day"), false)
                  , Todo("Fully Responsive  Bootstrap 3.0.2 Compatible", High, Some("Done"), false)
                  , Todo("Daily Standup Meeting", Medium, Some("Company"), false),
                    Todo("This is a test task", Low, Some("Full Day"), true))

  @dom
  override def content: Binding[Node] = {
    <section id="main-content">
      <section class="wrapper">
        <div class="row">
          <div class="col-md-12">
            <section class="card tasks-widget">
              <header class="card-header">
                <h2>Todo List</h2>
              </header>
              <div class="card-body">
                <div class="task-content">
                  <ul id="sortable" class="task-list">
                    {
                      Constants(tasks: _*).map { s =>
                        <li class={ if (s.complete) s"${s.prior.liClass} task-done" else s.prior.liClass }>
                          <i class=" fa fa-ellipsis-v"></i>
                          <div class="task-checkbox">
                            <input type="checkbox" class="list-child" value="" checked={ s.complete } />
                          </div>
                          <div class="task-title">
                            <span class="task-title-sp">{ s.title }</span>
                            <span class={ s"badge badge-sm ${s.prior.spanClass}" }>{ s.label.getOrElse("") }</span>
                            <div class="pull-right hidden-phone">
                              <button class="btn btn-success btn-xs fa fa-check"></button>
                              <button class="btn btn-primary btn-xs fa fa-pencil"></button>
                              <button class="btn btn-danger btn-xs fa fa-trash-o"></button>
                            </div>
                          </div>
                        </li>
                      }
                    }
                  </ul>
                </div>
                <div class=" add-task-row">
                  <a class="btn btn-success btn-sm pull-left" href="#">Add New Tasks</a>
                  <a class="btn btn-default btn-sm pull-right" href="#">See All Tasks</a>
                </div>
              </div>
            </section>
          </div>
        </div>
      </section>
    </section>
  }

  @dom
  override def install(): Unit = {
    import App.$
    $("input.list-child").change({(elem: HTMLElement) => {
      if ($(elem).is(":checked").asInstanceOf[Boolean]) {
        $(elem).parents("li").addClass("task-done")
      } else {
        $(elem).parents("li").removeClass("task-done")
      }
    }}: js.ThisFunction)

    $( "#sortable" ).sortable()
    $( "#sortable" ).disableSelection()
  }
}
