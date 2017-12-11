package com.ouspark.easysite
package components

import com.ouspark.easysite.models.TaskModel
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Var, Vars}
import com.thoughtworks.binding.{Binding, FutureBinding, dom}
import org.scalajs.dom.html.{Button, Table, TableRow}
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.dom.{Event, Node}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.util.{Failure, Success}

/**
  * Created by ouspark on 07/12/2017.
  */
object Table {


  case class Contact(name: Var[String], email: Var[String])

  val nulTh = js.Array(js.Dynamic.literal("id" -> 1, "name" -> "error", "label" -> "Error!"))
  val nulTd = js.Array(js.Dynamic.literal("error" -> "Get Data Error"))

  @dom
  def genThead(cols: Future[XMLHttpRequest], selectAll: Var[Boolean]) = {
    <tr>
      <th data:scope="col"><input type="checkbox" checked={ selectAll.bind } onclick={ event: Event => selectAll.value = !selectAll.value;}/></th>
      {
        FutureBinding(cols).bind match {
          case None =>
            <th data:scope="col">Loading</th>
            <!-- -->
          case Some(Success(response)) =>
            println(response.responseText)

            val resultList = JSON.parse(response.responseText).selectDynamic("export").selectDynamic("recordtype").asInstanceOf[js.Array[js.Dynamic]]
            Constants(resultList: _*).map { r =>
              <th data:scope="col">{ r.label.toString }</th>
            }
          case Some(Failure(exception)) =>
            <th data:scope="col">{ exception.toString }</th>
            <!-- -->
        }
      }
    </tr>
  }

  @dom
  def genTbody(cols: Future[XMLHttpRequest], rows: Future[XMLHttpRequest], dataCount: Var[Int], currPage: Var[Int]
               , selectAll: Var[Boolean]): Binding[BindingSeq[Node]] = {
    FutureBinding(rows).bind match {
      case None =>
        <tr><td>Loading...</td></tr>
        <!-- -->
      case Some(Success(rowList)) => {
        println(rowList.responseText)
        val rowJson = JSON.parse(rowList.responseText).asInstanceOf[js.Array[js.Dynamic]]
        dataCount.value = rowJson.length
        val start = (currPage.bind - 1) * 5
        val list = Var(rowJson.jsSlice(start, start + 5))

        Constants(list.bind: _*).map { r =>
          val checked = Var(r.selectDynamic("select").asInstanceOf[Boolean])
          FutureBinding(cols).bind match {
            case None =>
              <tr><td>Loading...</td></tr>
            case Some(Success(colList)) => {
              <tr>
                <td>
                  <input type="checkbox" checked={ checked.bind } onclick={event: Event =>
                      checked.value = !checked.value; r.updateDynamic("select")(checked.value);
                      if(!checked.value) selectAll.value = false}/>
                </td>
                {
                  val colJson = JSON.parse(colList.responseText).selectDynamic("export").selectDynamic("recordtype").asInstanceOf[js.Array[js.Dynamic]]
                  Constants(colJson: _*).map { s =>
                    <td>{ r.selectDynamic(s.name.toString).toString }</td>
                  }
                }
              </tr>
            }
            case Some(Failure(exception)) =>
              <tr><td>{ exception.toString }</td></tr>
          }
        }
      }
      case Some(Failure(exception)) =>
        <tr><td>{ exception.toString }</td></tr>
        <!-- -->
    }
  }

  @dom
  def genPagination(currPage: Var[Int], dataCount: Var[Int]): Binding[BindingSeq[Node]] = {

    val pageCount: Int = if(dataCount.bind % 5 == 0) dataCount.bind / 5 else dataCount.bind / 5 + 1
    val cntPerPage: Var[Int] = if(pageCount - (currPage.value / 5) * 5 >= 5) Var(5) else Var(pageCount % 5)
    val pagi: Var[Int] = Var(0)
    val init = Map("prev" -> (-1, 1), "next" -> (1, 0)) // prev&next button (-/+, first/last - cur mod 5)
    def paginate(p: String) = { event: Event =>
      import org.scalajs.dom._
      event.preventDefault()
      def setCurrValue = {
        val t = init.getOrElse(p, (0, 0))
        if(t._1 != 0) {
          if(t._2 == currPage.value % 5) {
            pagi.value += t._1
            cntPerPage.value = if(pageCount - pagi.value * 5 >= 5) 5 else pageCount % 5
          }
          currPage.value += t._1
        } else {
          currPage.value = p.toInt
        }
      }
      def activateClass(c: Int): Unit = {
        (1 to cntPerPage.value).foreach { s =>
          document.getElementById(s"pagi-item-#${s}").classList.remove("active")
        }
        document.getElementById(s"pagi-item-#${c}").classList.add("active")
      }
      def activatePNClass = {
        val prev = document.getElementById("pagi-prev")
        val next = document.getElementById("pagi-next")
        (currPage.value, pageCount) match {
          case (1, 1) => {
            prev.classList.add("disabled")
            next.classList.add("disabled")
          }
          case (1, _) => {
            prev.classList.add("disabled")
            next.classList.remove("disabled")
          }
          case (a, b) if(a == b) => {
            prev.classList.remove("disabled")
            next.classList.add("disabled")
          }
          case (_, _) => {
            prev.classList.remove("disabled")
            next.classList.remove("disabled")
          }
        }
      }

      setCurrValue
      activatePNClass
      activateClass(if(currPage.value % 5 == 0) 5 else currPage.value % 5 )
    }

    @dom
    def genLi(pagi: Int, cntPerPage: Int): Binding[BindingSeq[Node]] = {
      Constants(1 to cntPerPage: _*).map { c =>
        val disValue: Int = pagi * 5 + c
        <li id={ s"pagi-item-#${c}" } class={ if(c == currPage.value) "page-item active" else "page-item" }>
          <a class="page-link" href="#"  onclick={ paginate(disValue.toString) }>{ disValue.toString }</a>
        </li>
      }
    }

    <ul class="pagination">
      <li id="pagi-prev" class={ if(currPage.value == 1) "page-item disabled" else "page-item" }>
        <a class="page-link" href="#" onclick={ paginate("prev") } data:aria-label="Previous">
          <span data:aria-hidden="true">&laquo;</span>
          <span class="sr-only">Previous</span>
        </a>
      </li>
      { genLi(pagi.bind, cntPerPage.bind).bind }
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
  def genTable(cols: Future[XMLHttpRequest], rows: Future[XMLHttpRequest]): Binding[BindingSeq[Node]] = {
    val dataCount: Var[Int] = Var(1)
    val currPage: Var[Int] = Var(1)
    val selectAll: Var[Boolean] = Var(false)
    <table class="table table-hover">
      <thead>
        {
          genThead(cols, selectAll).bind
        }
      </thead>
      <tbody>
        {
          genTbody(cols, rows, dataCount, currPage, selectAll).bind
        }
      </tbody>
    </table>
    <nav>
      { genPagination(currPage, dataCount).bind }
    </nav>

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
