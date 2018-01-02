package com.ouspark.easysite.services

import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.XMLHttpRequest

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSON

/**
  * Created by ouspark on 07/12/2017.
  */
case class Feature(id: Int, name: String, label: String)
case class SideItem(feature: Seq[Feature])
object Api {

  import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
  val url = "http://localhost:5000/cpadmin/data/"

  def get(url: String): Future[XMLHttpRequest] = Ajax.get(url)


  def getFeatures(typ: String): Future[js.Array[js.Dynamic]] = Ajax.get(url + "feature.json").flatMap { s =>
      Future.successful(getFeatureList(typ, s.responseText))
  }
  def getTableMeta(typ: String, feature: String) = Ajax.get(url + "table.json").flatMap { s =>
    Future.successful(getTableMetaList(typ, feature, s.responseText))
  }

  def getDataList(typ: String, feature: String) = Ajax.get(url + "exp-cap-data.json").flatMap { s =>
    println(s.responseText)
    Future.successful(JSON.parse(s.responseText).asInstanceOf[js.Array[js.Dynamic]])
  }


  def getTableMetaList(typ:String, feature: String, response: String): js.Array[js.Dynamic] = {
    println(response)
    JSON.parse(response).selectDynamic(typ).selectDynamic(feature).asInstanceOf[js.Array[js.Dynamic]]

  }

  def getFeatureList(typ: String, response: String): js.Array[js.Dynamic] = {
    JSON.parse(response).selectDynamic(typ).asInstanceOf[js.Array[js.Dynamic]]
  }
}
