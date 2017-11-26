package com.ouspark.easyblog
package pages

import com.ouspark.easyblog.pages.Post.dOMParser
import com.ouspark.easyblog.routes.Space
import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Var, Vars}
import com.thoughtworks.binding.{Binding, FutureBinding, dom}
import org.scalajs.dom.ext.Ajax
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import org.scalajs.dom.{DOMParser, experimental, raw}
import org.scalajs.dom.raw.{Node}
import org.scalajs.dom.document

import scala.io.Source
import scala.util._

case class Post(name: String) extends Space {

  @dom
  override def render: Binding[Binding.BindingSeq[Node]] = {
    <section class="post">
      {
        import org.scalajs.dom.XMLHttpRequest
        import scala.concurrent.Future
        val future: Future[XMLHttpRequest] = Ajax.get("post/test-post.json")
        val githubResult: Binding[Option[Try[XMLHttpRequest]]] = FutureBinding(future)
        githubResult.bind match {
          case None =>
            <div>Loading</div>
          case Some(Success(xmlHttpRequest)) =>
            import scala.scalajs.js.JSON
            val json = JSON.parse(xmlHttpRequest.responseText)
            <div>
              {json.post.toString}
            </div>


          case Some(Failure(exception)) =>
            <div>{ exception.toString }</div>
        }
      }
    </section>
    <!-- -->
  }

  @dom
  def test: Binding[Node] = {
    <div></div>
  }

  val getLines: String = ""
//  {
//    val source = scala.io.Source.fromFile("file.txt")
//    source.mkString
//  }

  val varst = Var(getLines)

  @dom
  def getNode: Binding[Node] = {
    <div>
      {dOMParser.parseFromString({varst.bind}, "text/xml").childNodes.item(0)}
    </div>
  }

  @dom
  def getData = {
    import org.scalajs.dom.XMLHttpRequest
    import scala.concurrent.Future
    val future: Future[XMLHttpRequest] = Ajax.get(s"https://api.github.com/users/ouspark")
    val githubResult: Binding[Option[Try[XMLHttpRequest]]] = FutureBinding(future)
    githubResult.bind match {
      case None =>
        <div>Loading</div>
      case Some(Success(xmlHttpRequest)) =>
        val xml = xmlHttpRequest.responseText

      case Some(Failure(exception)) =>
        <div>{ exception.toString }</div>
    }
  }

//  @dom
//  def getPost: Binding[Node] = {
//    {getNode.bind}
//  }

}

object Post {

  val dOMParser: DOMParser = new DOMParser()
  val path = "post/"



}
