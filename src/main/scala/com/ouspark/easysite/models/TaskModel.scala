package com.ouspark.easysite.models

/**
  * Created by ouspark on 07/12/2017.
  */
case class TaskModel(name: String, taskStatus: String, scheduleDate: String, scheduleTime: String, scheduleStatus: String, description: String)

case class Todo(title: String, prior: Prior = Low, label: Option[String] = None, complete: Boolean = false)

case class Prior(liClass: String, spanClass: String)
object Critical extends Prior("list-danger", "label-danger")
object High extends Prior("list-warning", "label-warning")
object Medium extends Prior("list-info", "label-info")
object Low extends Prior("list-label", "label-inverse")