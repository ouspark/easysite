package com.ouspark.cpadmin

import upickle.default.{ReadWriter => RW, macroRW}

/**
  * Created by ouspark on 02/01/2018.
  */
object Model {

}

case class TaskModel(name: String, taskStatus: String, scheduleDate: String, scheduleTime: String, scheduleStatus: String, description: String)

case class Todo(title: String, prior: Prior = Low, label: String = "", complete: Boolean = false)
object Todo {
  implicit def rwTodo: RW[Todo] = macroRW
}

case class Prior(liClass: String, spanClass: String)
object Prior {
  implicit def rwPrior: RW[Prior] = macroRW

  val priorList = List(("critical", Critical), ("high", High), ("medium", Medium), ("low", Low))
}
object Critical extends Prior("list-danger", "label-danger")
object High extends Prior("list-warning", "label-warning")
object Medium extends Prior("list-info", "label-info")
object Low extends Prior("list-primary", "label-primary")

case class TableCol(id: Int, name: String, label: String)
case class TableMeta(typ: String, feature: String, cols: List[TableCol])
object TableCol {
  implicit def rwTableCol: RW[TableCol] = macroRW
}
object TableMeta {
  implicit def rwTableMeta: RW[TableMeta] = macroRW
}

case class DataRow(var select: Boolean = false, cols: Map[String, String] = Map.empty)
object DataRow {
  implicit def rwDataRow: RW[DataRow] = macroRW
}

case class DMFeature(feature: String, items: List[FeatureItem])
object DMFeature {
  implicit def rwDMFeature: RW[DMFeature] = macroRW
}

case class FeatureItem(id: Int, name: String, label:String)
object FeatureItem {
  implicit def rwFeatureItem: RW[FeatureItem] = macroRW
}
