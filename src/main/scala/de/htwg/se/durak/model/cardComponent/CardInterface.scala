package de.htwg.se.durak.model.cardComponent

import play.api.libs.json.JsObject

import scala.xml.Node

trait CardInterface {
  def toString: String

  def toXml: Node

  def toJson: JsObject
}
