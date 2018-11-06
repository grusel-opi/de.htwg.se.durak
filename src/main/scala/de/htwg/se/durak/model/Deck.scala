package de.htwg.se.durak.model

import scala.util.Random

case class Deck(cards: Set[Card]) {
  def this() = this(for {c <- CardColor.values; v <- CardValue.values} yield Card(c, v))

  def head: Card = cards.head

  def tail: Deck = Deck(cards.tail)

  def shuffle: Deck = Deck(Random.shuffle(cards))

  def schbebsi: Unit = print("schbebsii")

  def popTopCard(): (Card, Deck) = (cards.head, Deck(cards.tail))

  def popNCards(n: Int): (Set[Card], Deck) = (cards.slice(0, n - 1).toSet, Deck(cards.slice(n - 1, cards.size - 1)))

  override def toString: String = "Deck: " + cards.mkString(", ")

}
