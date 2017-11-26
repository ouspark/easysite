package com.ouspark.easyblog
package pages


import com.ouspark.easyblog.routes.Space
import com.thoughtworks.binding.{Binding, dom}
import com.thoughtworks.binding.Binding.BindingSeq
import org.scalajs.dom.raw.Node

import scala.scalajs.js

object Publisher extends Space {
  override def name: String = "publisher"

  @dom
  override def render: Binding[BindingSeq[Node]] = {
    <textarea></textarea>
    <!-- -->

  }






}
