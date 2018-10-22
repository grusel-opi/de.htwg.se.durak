package de.htwg.se.durak.model

import CardColor._
import CardValue._

case class Card(val color: CardColor, val value: CardValue) extends Ordered[Card] {
  override def toString(): String = value + " of " + color

  override def compare(that: Card): Int = {
    that match {
      case x if this.color.equals(that.color) => {
        x.value match {
          case y if this.value > y => 1
          case y if this.value < y => -1
          case _                   => 0
        }
      }
      case _ => 0
    }
  }
}
