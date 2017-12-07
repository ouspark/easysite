package com.ouspark.easysite
package components

import com.ouspark.easysite.models.TaskModel
import com.thoughtworks.binding.Binding.{BindingSeq, Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.{Event, Node}
import org.scalajs.dom.html.{Button, Table, TableRow}

/**
  * Created by ouspark on 07/12/2017.
  */
object Component {


  case class Contact(name: Var[String], email: Var[String])

  @dom
  def bindingButton(contact: Contact): Binding[Button] = {
    <button class="pure-button pure-button-primary"
    onclick={ event: Event =>
      contact.name.value = "Modified Name"
    }
    >
      Modify the name
    </button>
  }

  @dom
  def bindingTr(tpe: String, task: TaskModel): Binding[TableRow] = {
    <tr>
      <td data:scope="row"><a href={ s"#publisher/${tpe}/${task.name}/summary" } >{ task.name }</a></td>
      <td>{ task.taskStatus }</td>
      <td>{ task.scheduleDate + " - " + task.scheduleTime}</td>
      <td>{ task.scheduleStatus }</td>
      <td>{ task.description }</td>
      <td>{ actions.bind }</td>
    </tr>
  }

  @dom
  def bindingTable(tpe: String, tasks: BindingSeq[TaskModel]): Binding[Table] = {
    <table class="table table-hover">
      <thead>
        <tr>
          <th data:scope="col">Name</th>
          <th data:scope="col">Status</th>
          <th data:scope="col">Schedule Time</th>
          <th data:scope="col">Schedule Status</th>
          <th data:scope="col">Description</th>
          <th data:scope="col">Actions</th>
        </tr>
      </thead>
      <tbody>
        {
        for (task <- tasks) yield {
          bindingTr(tpe, task).bind
        }
        }
      </tbody>
    </table>
  }

  @dom
  def actions: Binding[Node] = {
    <div class="btn-group dropup">
      <button type="button" class="btn btn-light">
        Start
      </button>
      <button type="button" class="btn btn-light dropdown-toggle" data:data-toggle="dropdown" data:aria-haspopup="true" data:aria-expanded="false">
        <span class="sr-only">Toggle Dropdown</span>
      </button>
      <div class="dropdown-menu">
        <a class="dropdown-item" href="#">Action</a>
        <a class="dropdown-item" href="#">Another action</a>
        <a class="dropdown-item" href="#">Something else here</a>
      </div>
    </div>
  }

}
