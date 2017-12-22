package com.ouspark.easysite.models

import upickle.default.{ReadWriter => RW, macroRW}

/**
  * Created by ouspark on 07/12/2017.
  */
case class TaskModel(name: String, taskStatus: String, scheduleDate: String, scheduleTime: String, scheduleStatus: String, description: String)

case class Todo(title: String, prior: Prior = Low, label: String = "", complete: Boolean = false)
object Todo {
  implicit def rwTodo: RW[Todo] = macroRW
}

case class Prior(liClass: String, spanClass: String)
object Prior {
  implicit def rwPrior: RW[Prior] = macroRW
}
object Critical extends Prior("list-danger", "label-danger")
object High extends Prior("list-warning", "label-warning")
object Medium extends Prior("list-info", "label-info")
object Low extends Prior("list-label", "label-inverse")



