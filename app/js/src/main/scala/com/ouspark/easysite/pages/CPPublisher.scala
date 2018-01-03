package com.ouspark.easysite
package pages

import com.ouspark.cpadmin.FeatureItem
import com.ouspark.easysite.components.Card
import com.ouspark.easysite.components.Table.genTable
import com.ouspark.easysite.routes.{Space, SpaceRoute}
import com.ouspark.easysite.services.Api
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Vars}
import com.thoughtworks.binding.{Binding, FutureBinding, dom}
import org.scalajs.dom.ext.LocalStorage
import org.scalajs.dom.raw.Node

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}

import upickle.default._

class CPPublisher(pType: Option[String], taskName: Option[String], feature: Option[String]) extends Space {
  override def name: String = {
    if(pType.nonEmpty && taskName.nonEmpty && feature.nonEmpty) s"publisher/${pType.get}/${taskName.get}/${feature.get}"
    else "publisher"
  }

  val cardList = Vars(Card(1, "Standard Tasks", "standard"), Card(2, "Export Tasks", "export"), Card(3, "Import Tasks", "import"), Card(4, "Delete Tasks", "delete"))

  val featureStore = "features"
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
              LocalStorage(featureStore) = write[List[FeatureItem]](resultList)
              <div>
                {Constants(resultList: _*).map { r =>
                val className = if (SpaceRoute.pages.bind.name.endsWith(r.name)) "active" else ""
                <li>
                  <a href={s"#publisher/${pType.get}/${taskName.get}/${r.name}"} class={className}>
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

    def breadcrumb = Binding {
      if(!pType.isEmpty) {
        val features: List[FeatureItem] = read[List[FeatureItem]](LocalStorage(featureStore).getOrElse("{}"))
        <li class="breadcrumb-item active" data:aria-current="page">{ pType.get }</li>
        <li class="breadcrumb-item"><a href={ s"#publisher/${pType.get}/${taskName.get}/summary" }>{ taskName.get }</a></li>
        <li class="breadcrumb-item active" data:aria-current="page">{ features.filter(_.name == feature.get).head.label }</li>
      } else {
        <!-- -->
        <!-- -->
      }
    }
    <section id="main-content">
      <section class="wrapper">
        <div class="row">
          <div class="col-lg-12">
            <nav data:aria-label="breadcrumb" data:role="navigation">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href={ SpaceRoute.home.link }><i class="fa fa-home"></i> Home</a></li>
                <li class="breadcrumb-item"><a href="#publisher">Data Manager</a></li>
                { breadcrumb.bind }
              </ol>
            </nav>
          </div>
        </div>
        <div class="row">
         { mainDetail(pType, taskName).bind }
        </div>
      </section>
    </section>
  }


  @dom
  def mainDetail(pType: Option[String], taskName: Option[String]): Binding[BindingSeq[Node]] = {
    if(SpaceRoute.pages.bind.name.endsWith("publisher")) {
      <div class="col-md-12">
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
      </div>
      <!-- -->
    } else if(!SpaceRoute.pages.bind.name.endsWith("summary")) {
      val colsB = FutureBinding(Api.getTableMeta("export", "recordtype"))
      val rowsB = FutureBinding(Api.getDataList("todo", "todo"))
      val features: List[FeatureItem] = read[List[FeatureItem]](LocalStorage(featureStore).getOrElse(""))
      <div class="col-md-12">
        <h1>{ features.filter(_.name == feature.get).head.label }</h1>
        <div>
          {
            colsB.bind match {
              case None => <div>Loading...</div>
              case Some(Success(cols)) =>
                val resultList = cols.cols//JSON.parse(cols.responseText).selectDynamic("export").selectDynamic("recordtype").asInstanceOf[js.Array[js.Dynamic]]
                <div>{ genTable(resultList, rowsB).bind }</div>
              case Some(Failure(exception)) => <div>{ exception.toString }</div>
            }
          }
        </div>
      </div>
      <!-- -->
    }
    else {
      <div class="col-md-12">
        <h1>Summary</h1>
      </div>
      <!-- -->
    }
  }

}
