package com.ouspark.easysite
package pages

import com.ouspark.easysite.App.$
import com.ouspark.easysite.components.Modal
import com.ouspark.easysite.models._
import com.ouspark.easysite.routes.Space
import com.thoughtworks.binding.Binding.{Constants, Var, Vars}
import com.thoughtworks.binding.{Binding, FutureBinding, dom}
import org.scalajs.dom.{Event, File}
import org.scalajs.dom.ext.{Ajax, LocalStorage}
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement, Node}

import scala.languageFeature.reflectiveCalls
import scala.scalajs.js
import scala.util.{Failure, Success}

object Home extends Space {

  override def name: String = "home"
  val localStorage = "todos"

  @dom
  override def content: Binding[Node] = {
    val localTodo = LocalStorage(localStorage)
    var todos = Vars.empty[Todo]
    val editT = Var(Todo("", Low, ""))
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
                    def checkTodo(s: Todo, todos: Vars[Todo]) = { event: Event =>
                      val elem = event.currentTarget.asInstanceOf[HTMLInputElement]
                      todos.value(todos.value.indexOf(s)) = Todo(s.title, s.prior, s.label, elem.checked)
                      if ($(elem).is(":checked").asInstanceOf[Boolean]) {
                        $(elem).parents("li").addClass("task-done")
                      } else {
                        $(elem).parents("li").removeClass("task-done")
                      }
                    }
                    def editTodo(s: Todo, todos: Vars[Todo]) = { event: Event =>
                      editT.value = s
                      $("#addTaskModal").modal("show")
                    }
                    @dom
                    def item(s: Todo, todos: Vars[Todo]) = {
                      <li class={if (s.complete) s"${s.prior.liClass} task-done" else s.prior.liClass}>
                        <i class=" fa fa-ellipsis-v"></i>
                        <div class="task-checkbox">
                          <input type="checkbox" class="list-child" value="" checked={ s.complete } onclick={ checkTodo(s, todos) }/>
                        </div>
                        <div class="task-title">
                          <span class="task-title-sp">
                            {s.title}
                          </span>
                          <span class={ if (!s.label.isEmpty) s"badge badge-sm ${s.prior.spanClass}" else "" }>
                            {s.label}
                          </span>
                          <div class="pull-right hidden-phone">
                            <button class="btn btn-success btn-xs fa fa-check"></button>
                            <button class="btn btn-primary btn-xs fa fa-pencil" onclick={ editTodo(s, todos) }></button>
                            <button class="btn btn-danger btn-xs fa fa-trash-o" onclick={event: Event => todos.value.remove(todos.value.indexOf(s)) }></button>
                          </div>
                        </div>
                      </li>
                    }

                    if(!localTodo.isEmpty) {
                      import upickle.default._
                      todos = Vars[Todo](localTodo.toSeq.flatMap(read[Seq[Todo]]): _*)
                      def autoSave: Binding[Unit] = Binding{ LocalStorage(localStorage) = write(todos.all.bind) }
                      autoSave.watch()
                      Constants(todos.all.bind: _*).map { s =>
                        item(s, todos).bind
                      }
                    } else {
                      import scala.concurrent.ExecutionContext.Implicits.global
                      FutureBinding(Ajax.get("conf/data/todo.json")).bind match {
                        case None => <li>Loading...</li><!-- -->
                        case Some(Success(response)) =>
                          import upickle.default._
                          LocalStorage(localStorage) = response.responseText
                          todos = Vars[Todo](read[List[Todo]](response.responseText): _*)

                          def autoSave: Binding[Unit] = Binding{ LocalStorage(localStorage) = write(todos.all.bind) }
                          autoSave.watch()

                          Constants(todos.all.bind: _*).map { s =>
                            item(s, todos).bind
                          }

                        case Some(Failure(exception)) => <li>{ exception.toString }</li><!-- -->
                      }
                    }
                    }
                  </ul>
                </div>
                <div class=" add-task-row">
                  <a class="btn btn-success btn-sm pull-left" data:data-toggle="modal" href="#addTaskModal">Add New Tasks</a>
                  <a class="btn btn-default btn-sm pull-right" href="#">See All Tasks</a>
                </div>
                <div>
                  { Modal("addTaskModal", "lg", Modal.newTaskModal("addTaskModal", "Add New Task", editT.bind, todos)).bind }
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
//    import upickle.default._
//    val tasks = List(Todo("Flatlab is Modern Dashboard", Critical)
//      , Todo("Fully Responsive  Bootstrap 3.0.2 Compatible", High, "Done")
//      , Todo("Daily Standup Meeting", Medium, "Company"),
//      Todo("This is a test task", Low, "Full Day", true))
//    println(write[List[Todo]](tasks))
//    val str = """{"title":"Flatlab is Modern Dashboard","prior":{"liClass":"list-danger","spanClass":"label-danger"},"complete":false}"""
//    println(read[Todo](str).title)
  }
}
