package com.ouspark.easysite
package components

import com.ouspark.cpadmin._
import com.thoughtworks.binding.Binding.{Constants, Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{HTMLInputElement, HTMLSelectElement}
import org.scalajs.dom.{Event, Node}

object Modal {

  @dom
  def taskModal(id: String, header: String, size: String, todo: Todo, todos: Vars[Todo]): Binding[Node] = {
    val title: Var[String] = Var(todo.title)
    val label: Var[String] = Var(todo.label)
    val priority: Var[Prior] = Var(todo.prior)
    def addTask(title: String, label: String, priority: Prior) = {event: Event =>
      if(todo.title.isEmpty) {
        todos.value += Todo(title, priority, label)
      } else {
        todos.value(todos.value.indexOf(todo)) = Todo(title, priority, label, todo.complete)
      }
      import App.$
      $(s"#${id}").modal("hide")
    }

    @dom
    def priorSelector: Binding[Node] = {
      <select class="form-control sm-select" onchange={event: Event =>
        priority.value =
          event.currentTarget.asInstanceOf[HTMLSelectElement].value.toString match {
            case "default" => Low
            case "critical" => Critical
            case "high" => High
            case "medium" => Medium
            case "low" => Low
          }
      }>
        {
        Constants(Prior.priorList: _*).map { s =>
          <option value={ s._1 } selected={ if(todo.prior == s._2) true else false }>{ s._1.capitalize }</option>
        }
        }
      </select>
    }

    <div class="modal fade" id={ id } data:tabindex="-1" data:role="dialog" data:aria-labelledby="myModalLabel" data:aria-hidden="true">
      <div class={ s"modal-dialog modal-${size}"} >
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title">{ header }</h4>
            <button type="button" class="close" data:data-dismiss="modal" data:aria-hidden="true">&times;</button>
          </div>
          <div class="modal-body">
            <form class="form-inline" data:role="form">
              <div class="form-group">
                <label class="sr-only" for="exampleInputEmail2">Title</label>
                <input type="text" class="form-control sm-input" id="exampleInputEmail5" placeholder="Title here..." value={ title.bind }
                       onchange={event: Event => title.value = event.currentTarget.asInstanceOf[HTMLInputElement].value }/>
              </div>
              <div class="form-group">
                <label class="sr-only" for="exampleInputPassword2">Label</label>
                <input type="text" class="form-control sm-input" id="exampleInputPassword5" placeholder="Label" value={ label.bind }
                       onchange={event: Event => label.value = event.currentTarget.asInstanceOf[HTMLInputElement].value }/>
              </div>
              <div class="form-group">
                <label class="sr-only" for="inputSuccess">Priority</label>
                <div>
                  { priorSelector.bind }
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button data:data-dismiss="modal" class="btn btn-default" type="button">Close</button>
            <button class="btn btn-warning" type="button" onclick={ addTask(title.bind, label.bind, priority.bind) }> Confirm</button>
          </div>
        </div>
      </div>
    </div>
  }


}
