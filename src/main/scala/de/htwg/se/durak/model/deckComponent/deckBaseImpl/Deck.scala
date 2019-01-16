package de.htwg.se.durak.model.deckComponent.deckBaseImpl

import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.deckComponent.DeckInterface
import play.api.libs.json.{JsObject, Json}

import scala.util.Random
import scala.xml.Node

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

  def toXml: Node = {
    <deck>
      {cards.map(c => c.toXml)}
    </deck>
  }

  def toJson: List[JsObject] = {
    cards.map(c => c.toJson)
  }

  override def toString: String = "Deck: " + cards.mkString(", ")

}
