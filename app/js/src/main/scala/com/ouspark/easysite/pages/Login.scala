package com.ouspark.easysite
package pages

import com.ouspark.easysite.routes.Space
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.Event
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.Node

import scala.util.{Failure, Success}

object Login extends Space {

  override def name: String = "login"

  @dom
  override def header: Binding[Node] = {
    <!-- -->
  }

  @dom
  override def sidebar: Binding[Node] = {
    <!-- -->
  }

  @dom
  override def content: Binding[Node] = {
    def login(agency: String, user: String, pass: String) = { event: Event =>
      import upickle.default._
      import scala.concurrent.ExecutionContext.Implicits.global
      val json =write[Map[String, String]](Map("agency" -> agency, "userId" -> user, "password" -> pass))
      println(json)
//      val token = Ajax.post("http://192.168.0.15:3080/apis/v4/auth/agency", json, headers = Map("Access-Control-Allow-Origin" -> "*"))
//      token.onComplete {
//        case Success(response) => println(response.responseText)
//        case Failure(e) => println(e.toString)
//      }
    }
    <div id="main-content" style="margin: 0px">
      <form class="form-signin">
        <h2 class="form-signin-heading">sign in now</h2>
        <div class="login-wrap">
          <input type="text" class="form-control" placeholder="Agency" data:autofocus="true" />
          <input type="text" class="form-control" placeholder="User ID" />
          <input type="password" class="form-control" placeholder="Password" />
          <label class="checkbox">
            <input type="checkbox" value="remember-me" /> Remember me
            <span class="pull-right">
              <a data:data-toggle="modal" href="#myModal"> Forgot Password?</a>

            </span>
          </label>
          <button class="btn btn-lg btn-login btn-block" onclick={ login("FLAGSTAFF", "admin", "admin") }>Sign in</button>
          <p>or you can sign in via social network</p>
          <div class="login-social-link">
            <a href="index.html" class="facebook">
              <i class="fa fa-facebook"></i>
              Facebook
            </a>
            <a href="index.html" class="twitter">
              <i class="fa fa-twitter"></i>
              Twitter
            </a>
          </div>
          <div class="registration">
            Don't have an account yet?
            <a class="" href="registration.html">
              Create an account
            </a>
          </div>

        </div>

        <!-- Modal -->
        <div data:aria-hidden="true" data:aria-labelledby="myModalLabel" data:role="dialog" data:tabindex="-1" id="myModal" class="modal fade">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h4 class="modal-title">Forgot Password ?</h4>
                <button type="button" class="close" data:data-dismiss="modal" data:aria-hidden="true">&times;</button>
              </div>
              <div class="modal-body">
                <p>Enter your e-mail address below to reset your password.</p>
                <input type="text" name="email" placeholder="Email" autocomplete="off" class="form-control placeholder-no-fix" />

              </div>
              <div class="modal-footer">
                <button data:data-dismiss="modal" class="btn btn-default" type="button">Cancel</button>
                <button class="btn btn-success" type="button" onclick={event: Event => org.scalajs.dom.window.location.href = "#home"}>Submit</button>
              </div>
            </div>
          </div>
        </div>
        <!-- modal -->

      </form>
    </div>
  }
}
