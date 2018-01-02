package com.ouspark.easysite
package components

import com.ouspark.easysite.components.Table.bindingTable
import com.ouspark.cpadmin.TaskModel
import com.thoughtworks.binding.Binding.Vars
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Node

/**
  * Created by ouspark on 09/12/2017.
  */
object Card {

  val data = Vars(TaskModel("task_1", "On Hold", "12/07/2017", "04:05 PM", "Inactive", "This is first task")
    , TaskModel("task_2", "On Hold", "12/07/2017", "04:05 PM", "Inactive", "This is first task"))



  @dom
  def cards(card: Card): Binding[Node] = {
    val showClass = if(card.id == 1) "collapse show" else "collapse"
    <div class="card">
      <div class="card-header" data:role="tab" id="headingOne">
        <h5 class="mb-0">
          <a data:data-toggle="collapse" href={ s"#collapse${card.id.toString}" } data:aria-expanded="true" data:aria-controls={ s"collapse${card.id.toString}" }>
            { card.name }
          </a>
        </h5>
      </div>
      <div id={ s"collapse${card.id.toString}" } class={ showClass } data:role="tabpanel" data:aria-labelledby="headingOne" data:data-parent="#accordion">
        <div class="card-body">
          { bindingTable(card.tpe, data).bind }
        </div>
      </div>
    </div>
  }
}

case class Card(id: Int, name: String, tpe: String)
