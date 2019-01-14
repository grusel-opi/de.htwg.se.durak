package de.htwg.se.durak.model.playerComponent

import de.htwg.se.durak.model.cardComponent.Card
import play.api.libs.json.{JsObject, JsString, Json}

import scala.xml.Elem

case class Player(name: String, var handCards: List[Card]) {
  def this(name: String) = this(name: String, Nil)

  def pickCards(cards: List[Card]): Unit = handCards = handCards ::: cards

  def dropCards(cards: List[Card]): Unit = handCards = handCards.filterNot(elem => cards.contains(elem))

  def hasCard(card: Card): Boolean = handCards.contains(card)

  def sortHandCards(implicit ordering: Ordering[Card]): Unit = handCards = handCards.sorted

  def nameToXml: Elem = {
    <player>
      <name>
        {name}
      </name>
    </player>
  }

  def toXml: Elem = {
    <player>
      <name>
        {name}
      </name>
      <handCards>
        {handCards.map(c => c.toXml)}
      </handCards>
    </player>
  }

  def toJson: JsObject = {
    Json.obj(
      "player" -> Json.obj(
        "name" -> JsString(name),
        "handCards" -> handCards.map(c => c.toJson)
      )
    )
  }

  def nameToJson: JsObject = {
    Json.obj(
      "player" -> Json.obj(
        "name" -> JsString(name)
      )
    )
  }

  override def toString: String = name
}
