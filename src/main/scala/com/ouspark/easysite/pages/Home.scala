package com.ouspark.easysite
package pages

import com.ouspark.easysite.routes.Space
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Node

object Home extends Space {

  override def name: String = "home"

  @dom
  override def content: Binding[Node] = {
    <section id="main-content">
    <h1>Dashboard</h1>

    <section class="row text-center placeholders">
      <div class="col-6 col-sm-3 placeholder">
        <img src="data:image/gif;base64,R0lGODlhAQABAIABAAJ12AAAACwAAAAAAQABAAACAkQBADs=" data:width="200" data:height="200" class="img-fluid rounded-circle" alt="Generic placeholder thumbnail" />
        <h4>Label</h4>
        <div class="text-muted">Something else</div>
      </div>
      <div class="col-6 col-sm-3 placeholder">
        <img src="data:image/gif;base64,R0lGODlhAQABAIABAADcgwAAACwAAAAAAQABAAACAkQBADs=" data:width="200" data:height="200" class="img-fluid rounded-circle" alt="Generic placeholder thumbnail" />
        <h4>Label</h4>
        <span class="text-muted">Something else</span>
      </div>
      <div class="col-6 col-sm-3 placeholder">
        <img src="data:image/gif;base64,R0lGODlhAQABAIABAAJ12AAAACwAAAAAAQABAAACAkQBADs=" data:width="200" data:height="200" class="img-fluid rounded-circle" alt="Generic placeholder thumbnail" />
        <h4>Label</h4>
        <span class="text-muted">Something else</span>
      </div>
      <div class="col-6 col-sm-3 placeholder">
        <img src="data:image/gif;base64,R0lGODlhAQABAIABAADcgwAAACwAAAAAAQABAAACAkQBADs=" data:width="200" data:height="200" class="img-fluid rounded-circle" alt="Generic placeholder thumbnail" />
        <h4>Label</h4>
        <span class="text-muted">Something else</span>
      </div>
    </section>

    <h2>Section title</h2>
    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>#</th>
            <th>Header</th>
            <th>Header</th>
            <th>Header</th>
            <th>Header</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1,001</td>
            <td>Lorem</td>
            <td>ipsum</td>
            <td>dolor</td>
            <td>sit</td>
          </tr>
          <tr>
            <td>1,002</td>
            <td>amet</td>
            <td>consectetur</td>
            <td>adipiscing</td>
            <td>elit</td>
          </tr>
        </tbody>
      </table>
    </div>
      </section>
  }
}
