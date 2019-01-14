package de.htwg.se.durak.model.cardComponent

import CardColor.CardColor
import CardValue.CardValue
import play.api.libs.json.{JsObject, JsString, Json}

import scala.xml.Elem

case class Card(color: CardColor, value: CardValue) {

  def toXml: Elem = {
    <card>
      <color>
        {color}
      </color>
      <value>
        {value}
      </value>
    </card>
  }

  def toJson: JsObject = {
    Json.obj(
      "card" -> Json.obj(
        "color" -> JsString(color.toString),
        "value" -> JsString(value.toString)
      )
    )
  }

  override def toString(): String = color + " " + value
}
