package com.ouspark.easysite
package pages

import com.ouspark.easysite.components.Component.bindingTable
import com.ouspark.easysite.models.TaskModel
import com.ouspark.easysite.routes.{Space, SpaceRoute}
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Node

class CPPublisher(pType: Option[String], taskName: Option[String], feature: Option[String]) extends Space {
  override def name: String = {
    if(pType.nonEmpty && taskName.nonEmpty && feature.nonEmpty) s"publisher/${pType.get}/${taskName.get}/${feature.get}"
    else "publisher"
  }

  val cardList = Vars(Card(1, "Standard Tasks", "standard"), Card(2, "Export Tasks", "export"), Card(3, "Import Tasks", "import"), Card(4, "Delete Tasks", "delete"))
  val data = Vars(TaskModel("task_1", "On Hold", "12/07/2017", "04:05 PM", "Inactive", "This is first task")
    , TaskModel("task_2", "On Hold", "12/07/2017", "04:05 PM", "Inactive", "This is first task"))

  case class Card(id: Int, name: String, tpe: String)



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

  @dom
  override def render: Binding[BindingSeq[Node]] = {

    val mainClass = if(pType.nonEmpty && taskName.nonEmpty) "col-sm-9 ml-sm-auto col-md-10 pt-3" else "col-sm-9 col-md-10 offset-md-1"
    <div class="row">

       { navDiv(pType, taskName).bind }

      <main data:role="main" class={ mainClass }>
        <h1>Data Manager</h1>
        <div class="table-responsive">
          <div id="accordion" data:role="tablist">
            {
            for(card <- cardList) yield {
              cards(card).bind
            }
            }
          </div>
        </div>
      </main>
    </div>
    <!-- -->
  }

  @dom
  def navDiv(pType: Option[String], taskName: Option[String]): Binding[BindingSeq[Node]] = {
    if(pType.nonEmpty && taskName.nonEmpty) {
      <nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
        <ul class="nav nav-pills flex-column">
          {
          Constants(SpaceRoute.routes: _*).map { s =>
            val className = if (SpaceRoute.pages.bind.name == s.name) "nav-link active" else "nav-link"
            <li class="nav-item">
              <a href={s.link} class={className}>{s.name.capitalize}</a>
            </li>
          }
          }
        </ul>
      </nav>
      <!-- -->
    }
    else {
      <!-- -->
      <!-- -->
    }
  }
}
