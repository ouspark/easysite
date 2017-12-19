package com.ouspark.easysite
package pages

import com.ouspark.easysite.routes.Space
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{HTMLElement, Node}

import scala.scalajs.js

object Home extends Space {

  override def name: String = "home"

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
                    <li class="list-primary">
                      <i class=" fa fa-ellipsis-v"></i>
                      <div class="task-checkbox">
                        <input type="checkbox" class="list-child" value=""  />
                      </div>
                      <div class="task-title">
                        <span class="task-title-sp">Flatlab is Modern Dashboard</span>
                        <span class="badge badge-sm label-success">2 Days</span>
                        <div class="pull-right hidden-phone">
                          <button class="btn btn-success btn-xs fa fa-check"></button>
                          <button class="btn btn-primary btn-xs fa fa-pencil"></button>
                          <button class="btn btn-danger btn-xs fa fa-trash-o"></button>
                        </div>
                      </div>
                    </li>
                    <li class="list-danger">
                      <i class=" fa fa-ellipsis-v"></i>
                      <div class="task-checkbox">
                        <input type="checkbox" class="list-child" value=""  />
                      </div>
                      <div class="task-title">
                        <span class="task-title-sp"> Fully Responsive  Bootstrap 3.0.2 Compatible </span>
                        <span class="badge badge-sm label-danger">Done</span>
                        <div class="pull-right hidden-phone">
                          <button class="btn btn-success btn-xs fa fa-check"></button>
                          <button class="btn btn-primary btn-xs fa fa-pencil"></button>
                          <button class="btn btn-danger btn-xs fa fa-trash-o"></button>
                        </div>
                      </div>
                    </li>
                    <li class="list-success">
                      <i class=" fa fa-ellipsis-v"></i>
                      <div class="task-checkbox">
                        <input type="checkbox" class="list-child" value=""  />
                      </div>
                      <div class="task-title">
                        <span class="task-title-sp"> Daily Standup Meeting </span>
                        <span class="badge badge-sm label-warning">Company</span>
                        <div class="pull-right hidden-phone">
                          <button class="btn btn-success btn-xs fa fa-check"></button>
                          <button class="btn btn-primary btn-xs fa fa-pencil"></button>
                          <button class="btn btn-danger btn-xs fa fa-trash-o"></button>
                        </div>
                      </div>
                    </li>
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
    println("inside home")
    import App.$
    $("input.list-child").change({(elem: HTMLElement) => {
      if ($(elem).is(":checked").asInstanceOf[Boolean]) {
        $(elem).parents("li").addClass("task-done")
      } else {
        $(elem).parents("li").removeClass("task-done")
      }
    }}: js.ThisFunction)

  }
}
