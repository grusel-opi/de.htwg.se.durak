package de.htwg.se.durak.model.cardComponent

import de.htwg.se.durak.model.cardComponent.cardBaseImpl.CardColor.CardColor
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.CardValue.CardValue
import play.api.libs.json.JsObject

import scala.xml.Node

trait CardInterface {
  def color: CardColor

  def value: CardValue

  def toString: String

  def toXml: Node

  def toJson: JsObject
}
