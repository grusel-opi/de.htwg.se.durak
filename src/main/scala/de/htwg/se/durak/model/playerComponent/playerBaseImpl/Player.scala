package de.htwg.se.durak.model.playerComponent.playerBaseImpl

import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.CardColor.CardColor
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.durak.model.playerComponent.PlayerInterface
import play.api.libs.json.{JsObject, JsString, Json}

import scala.xml.Node

case class Player(name: String, var handCards: List[CardInterface]) extends PlayerInterface {
  def this(name: String) = this(name: String, Nil)

  override def toString: String = name

  def pickCards(cards: List[CardInterface]): Unit = handCards = handCards ::: cards

  def dropCards(cards: List[CardInterface]): Unit = handCards = handCards.filterNot(elem => cards.contains(elem))

  def hasCard(card: CardInterface): Boolean = handCards.contains(card)

  def sortHandCards(): Unit = {
    var sorted_hand_cards: List[CardInterface] = List()
    var card_colors: List[CardColor] = List.empty

    // get card colors
    handCards.foreach(card => {
      if (!card_colors.contains(card.color)) {
        card_colors = card_colors ::: List(card.color)
      }
    })

    // sort cards by color
    card_colors.foreach(color => {
      handCards.foreach(card => {
        if (card.color == color) {
          sorted_hand_cards = sorted_hand_cards ::: List(card)
        }
      })
    })

    // sort cards by value
    for (i <- sorted_hand_cards.indices) {
      if (i + 1 < sorted_hand_cards.length && sorted_hand_cards(i).color == sorted_hand_cards(i + 1).color) {
        if (sorted_hand_cards(i).value > sorted_hand_cards(i + 1).value) {
          sorted_hand_cards.updated(i, sorted_hand_cards(i + 1)).updated(i + 1, sorted_hand_cards(i))
        }
      }
    }

    handCards = sorted_hand_cards
  }

  def nameToXml: Node = {
    <player>
      <name>
        {name}
      </name>
    </player>
  }

  def toXml: Node = {
    <player>
      <name>
        {name}
      </name>
      <handCards>
        {handCards.map(c => c.toXml)}
      </handCards>
    </player>
  }

  def nameToJson: JsObject = {
    Json.obj(
      "player" -> Json.obj(
        "name" -> JsString(name)
      )
    )
  }

  def toJson: JsObject = {
    Json.obj(
      "player" -> Json.obj(
        "name" -> JsString(name),
        "handCards" -> handCards.map(c => c.toJson)
      )
    )
  }
}
