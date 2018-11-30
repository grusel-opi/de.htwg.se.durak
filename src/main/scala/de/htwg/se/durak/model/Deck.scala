package de.htwg.se.durak.model

import scala.util.Random

case class Deck(cards: List[Card]) {
  def this() = this(for {c <- CardColor.values.toList; v <- CardValue.values} yield Card(c, v))

  def lastCard: Card = cards.last

  def shuffle: Deck = Deck(Random.shuffle(cards))

  def popTopCard(): (Card, Deck) = (cards.head, Deck(cards.tail))

  def popNCards(n: Int): (List[Card], Deck) = (cards.slice(0, n).toList, Deck(cards.slice(n, cards.size)))

  override def toString: String = "Deck: " + cards.mkString(", ")

}
