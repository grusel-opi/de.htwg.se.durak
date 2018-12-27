package de.htwg.se.durak.model

import scala.util.Random

case class Deck(cards: List[Card]) {
  def this() = this(for {c <- CardColor.values.toList; v <- CardValue.values.toList} yield Card(c, v))

  def head: Card = cards.head

  def tail: Deck = Deck(cards.tail)

  def shuffle: Deck = Deck(Random.shuffle(cards))

  def popTopCard(): (Card, Deck) = (cards.head, Deck(cards.tail))

  def popNCards(n: Int): (List[Card], Deck) = {
    if (n >= cards.size) {
      (cards, Deck(Nil))
    } else {
      (cards.slice(0, n), Deck(cards.slice(n, cards.size)))
    }
  }

  override def toString: String = "Deck: " + cards.mkString(", ")

}
