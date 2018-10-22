package de.htwg.se.durak.model

import scala.util.Random

case class Deck(var cards: List[Card]) {

  def this() = this(for {c <- CardColor.values.toList; v <- CardValue.values} yield Card(c, v))

  def shuffle: Deck = new Deck(Random.shuffle(cards))

  def popTopCard(): Card = {
    val head::tail = cards
    cards = tail
    head
  }

  override def toString: String = "Deck: " + cards.mkString(", ")

}
