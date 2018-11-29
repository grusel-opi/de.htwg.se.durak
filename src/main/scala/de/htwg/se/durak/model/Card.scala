package de.htwg.se.durak.model

import de.htwg.se.durak.model.CardColor.CardColor
import de.htwg.se.durak.model.CardValue.CardValue

case class Card(color: CardColor, value: CardValue) {
  override def toString(): String =  color + " " + value
}
