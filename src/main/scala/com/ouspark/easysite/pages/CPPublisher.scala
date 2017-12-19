package com.ouspark.easysite
package pages

import com.ouspark.easysite.components.Card
import com.ouspark.easysite.components.Table.genTable
import com.ouspark.easysite.routes.{Space, SpaceRoute}
import com.ouspark.easysite.services.Api
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Vars}
import com.thoughtworks.binding.{Binding, FutureBinding, dom}
import org.scalajs.dom.raw.Node

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.util.{Failure, Success}

class CPPublisher(pType: Option[String], taskName: Option[String], feature: Option[String]) extends Space {
  override def name: String = {
    if(pType.nonEmpty && taskName.nonEmpty && feature.nonEmpty) s"publisher/${pType.get}/${taskName.get}/${feature.get}"
    else "publisher"
  }

  val cardList = Vars(Card(1, "Standard Tasks", "standard"), Card(2, "Export Tasks", "export"), Card(3, "Import Tasks", "import"), Card(4, "Delete Tasks", "delete"))

  @dom
  override def sidebar: Binding[Node] = {
    if(pType.nonEmpty && taskName.nonEmpty) {
      <aside>
        <div id="sidebar" class="nav-collapse ">
          <ul class="sidebar-menu" id="nav-accordion">
            {FutureBinding(Api.getFeatures(pType.get)).bind match {
            case None =>
              <div>Loading</div>
            case Some(Success(resultList)) =>
              <div>
                {Constants(resultList: _*).map { r =>
                val className = if (SpaceRoute.pages.bind.name.endsWith(r.name.toString)) "active" else ""
                <li>
                  <a href={s"#publisher/${pType.get}/${taskName.get}/${r.name.toString}"} class={className}>
                    {r.label.toString}
                  </a>
                </li>
              }}
              </div>
            case Some(Failure(exception)) =>
              <div>
                {exception.toString}
              </div>
          }}
          </ul>
        </div>
      </aside>
    } else {
      Space.sidebar.bind
    }
  }


  @dom
  override def content: Binding[Node] = {

    <section id="main-content">
      <section class="wrapper">
        <div class="row">
          <div class="col-md-12">
            { mainDetail(pType, taskName).bind }
          </div>
        </div>
      </section>
    </section>
  }


  @dom
  def mainDetail(pType: Option[String], taskName: Option[String]): Binding[BindingSeq[Node]] = {
    if(SpaceRoute.pages.bind.name.endsWith("publisher")) {
      <h1>Data Manager</h1>
      <div class="table-responsive">
        <div id="accordion" data:role="tablist">
          {
          for(card <- cardList) yield {
            Card.cards(card).bind
          }
          }
        </div>
      </div>
    } else if(!SpaceRoute.pages.bind.name.endsWith("summary")) {
      val colsB = FutureBinding(Api.get("conf/data/table.json"))
      val rowsB = FutureBinding(Api.get("conf/data/exp-cap-data.json"))
      <h1>{ feature.get }</h1>
      <div>
        {
          colsB.bind match {
            case None => <div>Loading...</div>
            case Some(Success(cols)) =>
              val resultList = JSON.parse(cols.responseText).selectDynamic("export").selectDynamic("recordtype").asInstanceOf[js.Array[js.Dynamic]]
              <div>{ genTable(resultList, rowsB).bind }</div>
            case Some(Failure(exception)) => <div>{ exception.toString }</div>
          }
        }
      </div>
    }
    else {
      <h1>{ feature.get }</h1>
      <!-- -->
    }
  }


  @dom
  def navDiv(pType: Option[String], taskName: Option[String]): Binding[BindingSeq[Node]] = {
    if(pType.nonEmpty && taskName.nonEmpty) {
      <nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
        <ul class="nav nav-pills flex-column">
          {

            FutureBinding(Api.getFeatures(pType.get)).bind match {
              case None =>
                <div>Loading</div>
              case Some(Success(resultList)) =>
                <div>{
                  Constants(resultList: _*).map { r =>
                    val className = if (SpaceRoute.pages.bind.name.endsWith(r.name.toString)) "nav-link active" else "nav-link"
                    <li class="nav-item">
                      <a href={ s"#publisher/${pType.get}/${taskName.get}/${r.name.toString}" } class={className}>{r.label.toString}</a>
                    </li>
                    }
                  }
                </div>
              case Some(Failure(exception)) =>
                <div>{ exception.toString }</div>
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
