package com.ouspark.easysite
package components

import com.ouspark.cpadmin.TaskModel
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Var}
import com.thoughtworks.binding.{Binding, FutureBinding, dom}
import org.scalajs.dom.html.{Table, TableRow}
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.dom.{Event, Node}

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.util.{Failure, Success}

/**
  * Created by ouspark on 07/12/2017.
  */
object Table {

  @dom
  def genTable(cols: js.Array[js.Dynamic], rows: FutureBinding[XMLHttpRequest]): Binding[BindingSeq[Node]] = {
    val dataCount: Var[Int] = Var(1)
    val currPage: Var[Int] = Var(1)
    val selectAll: (Var[Int], Var[Boolean]) = (Var(0), Var(false))
    val pageSize = 10  // Row count per page
    <table class="table table-hover">
      <thead>
        {
          <tr>
            <th data:scope="col"><input type="checkbox" checked={ selectAll._2.bind } onclick={ event: Event =>
              selectAll._2.value = !selectAll._2.value;   // (Int, Boolean) => Int -> 0: default, -1: de-select, 1: select
              selectAll._1.value = if(selectAll._2.value) 1 else if(selectAll._1.value == 1) -1 else 0; }/></th>
            {
              Constants(cols: _*).map { r =>
                <th data:scope="col">{ r.label.toString }</th>
              }
            }
          </tr>
        }
      </thead>
      <tbody>
        {
          rows.bind match {
            case None =>
              <tr><td>Loading...</td></tr>
              <!-- -->
            case Some(Success(rowList)) =>
              val rowJson = JSON.parse(rowList.responseText).asInstanceOf[js.Array[js.Dynamic]]
              dataCount.value = rowJson.length
              val start = (currPage.bind - 1) * pageSize
              val list = Var(rowJson.jsSlice(start, start + pageSize))

              Constants(list.bind: _*).map { r =>
                val checked = Var(r.selectDynamic("select").asInstanceOf[Boolean])
                selectAll._1.bind match {
                  case -1 => r.updateDynamic("select")(false); checked.value = false;
                  case 1 => r.updateDynamic("select")(true); checked.value = true;
                  case _ => r.updateDynamic("select")(checked.value);
                }
                <tr>
                  <td>
                    <input type="checkbox" checked={ checked.bind } onclick={event: Event =>
                    checked.value = !checked.value; selectAll._1.value = 0
                    if(!checked.value) selectAll._2.value = false; }/>
                  </td>
                  {
                    Constants(cols: _*).map { s =>
                      <td>{ r.selectDynamic(s.name.toString).toString }</td>
                    }
                  }
                </tr>
              }
            case Some(Failure(exception)) =>
              <tr><td>{ exception.toString }</td></tr>
              <!-- -->
          }
        }
      </tbody>
    </table>
    <nav class="dataTables_paginate paging_bootstrap pagination">
      { genPagination(currPage, dataCount, pageSize).bind }
    </nav>

  }

  @dom
  def genPagination(currPage: Var[Int], dataCount: Var[Int], pageSize: Int): Binding[BindingSeq[Node]] = {
    val pageLis = 5  // how many pages display in pagination bar
    val pageCount: Int = if(dataCount.bind % pageSize == 0) dataCount.bind / pageSize else dataCount.bind / pageSize + 1
    val pagi: Var[Int] = Var(currPage.value / pageSize)
    val cntPerPage: Var[Int] = if(pageCount - pagi.bind * pageSize >= pageLis) Var(pageLis) else Var(pageCount % pageLis)
    val init = Map("prev" -> (-1, 1), "next" -> (1, 0)) // prev&next button (-/+, first/last - cur mod 5)
    def paginate(p: String) = { event: Event =>
      import org.scalajs.dom._
      event.preventDefault()
      def setCurrValue() = {
        val t = init.getOrElse(p, (0, 0))
        if(t._1 != 0) {
          if(t._2 == currPage.value % pageLis) {
            pagi.value += t._1
            cntPerPage.value = if(pageCount - pagi.value * pageSize >= pageLis) pageLis else pageCount % pageLis
          }
          currPage.value += t._1
        } else {
          currPage.value = p.toInt
        }
      }
      def activateClass(c: Int): Unit = {
        (1 to cntPerPage.value).foreach { s =>
          document.getElementById("pagi-item-#" + s).classList.remove("active")
        }
        document.getElementById("pagi-item-#" + c).classList.add("active")
      }
      def activatePNClass() = {
        val prev = document.getElementById("pagi-prev")
        val next = document.getElementById("pagi-next")
        (currPage.value, pageCount) match {
          case (1, 1) =>
            prev.classList.add("disabled")
            next.classList.add("disabled")
          case (1, _) =>
            prev.classList.add("disabled")
            next.classList.remove("disabled")
          case (a, b) if a == b =>
            prev.classList.remove("disabled")
            next.classList.add("disabled")
          case (_, _) =>
            prev.classList.remove("disabled")
            next.classList.remove("disabled")
        }
      }

      setCurrValue()
      activatePNClass()
      activateClass(if(currPage.value % pageLis == 0) pageLis else currPage.value % pageLis )
    }

    <ul class="pagination">
      <li id="pagi-prev" class={ if(currPage.value == 1) "page-item disabled" else "page-item" }>
        <a class="page-link" href="#" onclick={ paginate("prev") } data:aria-label="Previous">
          <span data:aria-hidden="true">&laquo;</span>
          <span class="sr-only">Previous</span>
        </a>
      </li>
      {
      Constants(1 to cntPerPage.bind: _*).map { c =>
        val disValue: Int = pagi.bind * pageLis + c
        <li id={ "pagi-item-#" + c } class={ if(c == currPage.value) "page-item active" else "page-item" }>
          <a class="page-link" href="#"  onclick={ paginate(disValue.toString) }>{ disValue.toString }</a>
        </li>
      }
      }
      <li id="pagi-next" class="page-item">
        <a class="page-link" href="#" onclick={ paginate("next") } data:aria-label="Next">
          <span data:aria-hidden="true">&raquo;</span>
          <span class="sr-only">Next</span>
        </a>
      </li>
    </ul>
    <!-- -->
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
