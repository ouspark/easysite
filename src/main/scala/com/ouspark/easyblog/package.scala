package com.ouspark

import com.thoughtworks.binding.Binding

package object easyblog {
  implicit def makeIntellijHappy[T <: org.scalajs.dom.raw.Node](x: scala.xml.Node): Binding[T] =
    throw new AssertionError("This should never execute")
  implicit def makeIntellijHappy1[T <: org.scalajs.dom.raw.Node](x: scala.xml.NodeBuffer): Binding[T] =
    throw new AssertionError("This should never execute.")
}
