package de.htwg.se.durak.model.cardComponent

import CardColor.CardColor
import CardValue.CardValue

case class Card(color: CardColor, value: CardValue) {
  override def toString(): String =  color + " " + value
}
