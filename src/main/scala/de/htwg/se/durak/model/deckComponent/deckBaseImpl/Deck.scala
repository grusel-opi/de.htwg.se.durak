package de.htwg.se.durak.model.deckComponent.deckBaseImpl

import de.htwg.se.durak.model.cardComponent.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.deckComponent.DeckInterface

import scala.util.Random

case class Deck(cards: List[Card]) extends DeckInterface {
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
