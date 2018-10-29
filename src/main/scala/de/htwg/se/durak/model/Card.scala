package de.htwg.se.durak.model

import de.htwg.se.durak.model.CardColor.CardColor
import de.htwg.se.durak.model.CardValue.CardValue

case class Card(color: CardColor, value: CardValue) extends Ordered[Card] {
  override def toString(): String = value + " of " + color
  override def compare(that: Card): Int = that match {
      case x if this.color.equals(that.color) => this.value.compare(x.value)
      case _ => 0
  }
}
