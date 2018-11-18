package de.htwg.se.durak.model

import scala.util.Random

// können kein für cards nicht Set[Card] als Typ verwenden, da dann shuffle nicht funktioniert.
case class Deck(cards: List[Card]) {
  def this() = this(for {c <- CardColor.values.toList; v <- CardValue.values} yield Card(c, v))

  def getLastCard: Card = cards.last

  def shuffle: Deck = Deck(Random.shuffle(cards))

  def popTopCard(): (Card, Deck) = (cards.head, Deck(cards.tail))

  def popNCards(n: Int): (List[Card], Deck) = (cards.slice(0, n - 1), Deck(cards.slice(n - 1, cards.size - 1)))

  override def toString: String = "Deck: " + cards.mkString(", ")

}
