package com.ouspark.easysite
package components

import com.ouspark.easysite.models.TaskModel
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Var}
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
  def genThead(cols: Future[XMLHttpRequest]) = {
    val selectable = true
    <tr>
      <th data:scope="col"><input type="checkbox" /></th>
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
  def genTbody(cols: Future[XMLHttpRequest], rows: Future[XMLHttpRequest], dataCount: Var[Int]): Binding[BindingSeq[Node]] = {
    val selectable = true
    FutureBinding(rows).bind match {
      case None =>
        <tr><td>Loading...</td></tr>
        <!-- -->
      case Some(Success(rowList)) => {
        println(rowList.responseText)
        val rowJson = JSON.parse(rowList.responseText).asInstanceOf[js.Array[js.Dynamic]]
        dataCount.value = 18
        Constants(rowJson: _*).map { r =>
          FutureBinding(cols).bind match {
            case None =>
              <tr><td>Loading...</td></tr>
            case Some(Success(colList)) => {
              <tr>
                <td><input type="checkbox" /></td>
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

  def activePagiClass(c: Int) = {
    import org.scalajs.dom._
    (1 to 5).foreach { s =>
      val elem = document.getElementById(s"pagi-item-#${s}")
      if(elem != null) elem.classList.remove("active")
    }
    document.getElementById(s"pagi-item-#${c}").classList.add("active")
  }

  @dom
  def genPagination(dataCount: Var[Int]): Binding[BindingSeq[Node]] = {

    val pageCount: Int = if(dataCount.bind % 5 == 0) dataCount.bind / 5 else dataCount.bind / 5 + 1
    val currPage: Var[Int] = Var(1)
    val prevClass: String = if(currPage.bind == 1) "page-item disabled" else "page-item"
    val nextClass: String = if(currPage.bind == pageCount) "page-item disabled" else "page-item"
      <ul class="pagination">
        <li class={ prevClass }>
          <a class="page-link" href="#"
             onclick={event: Event => {
               event.preventDefault()
               currPage.value -= 1
               activePagiClass(currPage.value % 5)
             }
             } data:aria-label="Previous">
            <span data:aria-hidden="true">&laquo;</span>
            <span class="sr-only">Previous</span>
          </a>
        </li>
        {
        Constants(1 to 5: _*).map { c =>
          <li id={ s"pagi-item-#${c}" } class="page-item"><a class="page-link" href="#"
                                          onclick={ event: Event => {
                                            event.preventDefault();
                                            currPage.value = c;
                                            activePagiClass(c)
                                          }}>{ c.toString }</a>
          </li>
        }

        }
        <li class={ nextClass }>
          <a class="page-link" href="#"
             onclick={event: Event => {
               event.preventDefault()
               currPage.value += 1
               activePagiClass(currPage.value % 5)
             }
             } data:aria-label="Next">
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
    <table class="table table-hover">
      <thead>
        {
          genThead(cols).bind
        }
      </thead>
      <tbody>
        {
          genTbody(cols, rows, dataCount).bind
        }
      </tbody>
    </table>
    <nav>
      { genPagination(dataCount).bind }
    </nav>

  }




  @dom
  def bindingButton(contact: Contact): Binding[Button] = {
    <button class="pure-button pure-button-primary"
    onclick={ event: Event =>
      contact.name.value = "Modified Name"
    }
    >
      Modify the name
    </button>
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
