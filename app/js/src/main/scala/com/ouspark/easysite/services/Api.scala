package com.ouspark.easysite.services

import com.ouspark.cpadmin._
import org.scalajs.dom.ext.Ajax
import upickle.default._

import scala.concurrent.Future

/**
  * Created by ouspark on 07/12/2017.
  */
object Api {

  import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
  val biz = "http://localhost:5000"
  val resources = "/cpadmin/data"
  val apis = "/apis/v1"

  def getTodos: Future[List[Todo]] = Ajax.get(biz + apis + "/todos").map { response =>
    read[List[Todo]](response.responseText)
  }
  def getFeatures(typ: String): Future[List[FeatureItem]] = Ajax.get(biz + resources + "/feature.json").map{ response =>
    read[List[DMFeature]](response.responseText).filter(_.feature == typ).head.items
  }
  def getTableMeta(typ: String, feature: String): Future[TableMeta] = Ajax.get(biz + resources + "/table.json").map { response =>
    read[List[TableMeta]](response.responseText).filter(_.typ == typ).filter(_.feature == feature).head
  }
  def getDataList(typ: String, feature: String): Future[List[DataRow]] = Ajax.get(biz + resources + "/exp-cap-data.json").map { response =>
    read[List[DataRow]](response.responseText)
  }

}
