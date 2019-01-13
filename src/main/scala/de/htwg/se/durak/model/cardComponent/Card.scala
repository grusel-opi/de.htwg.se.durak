package de.htwg.se.durak.model.cardComponent

import CardColor.CardColor
import CardValue.CardValue

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

  override def toString(): String = color + " " + value
}
