package de.htwg.se.durak.model.gameComponent.gameBaseImpl

import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.model.playerComponent.Player
import play.api.libs.json.{JsObject, Json}

import scala.xml.{Elem, NodeBuffer}

case class Turn(attacker: Player, victim: Player, neighbour: Player, attackCards: List[Card], blockedBy: Map[Card, Card]) {

  def this(attacker: Player, victim: Player, neighbor: Player)
  = this(attacker: Player, victim: Player, neighbor: Player, Nil, Map())

  def addBlockCard(attackCard: Card, blockCard: Card): Turn = copy(attacker, victim, neighbour,
    attackCards.filterNot(c => c.equals(attackCard)), blockedBy + (attackCard -> blockCard))

  def addAttackCard(card: Card): Turn = copy(attacker, victim, neighbour, attackCards ::: List(card))

  def getCards: List[Card] = attackCards ::: blockedBy.values.toList ::: blockedBy.keys.toList

  def getPlayers: List[Player] = List(attacker, victim, neighbour)

  def toXml:NodeBuffer = {
      <attacker>
        {attacker.nameToXml}
      </attacker>
      <victim>
        {victim.nameToXml}
      </victim>
      <neighbour>
        {neighbour.nameToXml}
      </neighbour>
      <attackCards>
        {attackCards.map(c => c.toXml)}
      </attackCards>
      <blockedBy>
        {blockedBy.map(entry => blockedByMapEntryToXml(entry))}
      </blockedBy>
  }

  def blockedByMapEntryToXml(entry: (Card, Card)): NodeBuffer = {
    <attackCards>
      {entry._1.toXml}
    </attackCards>
      <blockingCards>
        {entry._2.toXml}
      </blockingCards>
  }

  def toJson: JsObject = {
    Json.obj(
      "attacker" -> attacker.nameToJson,
      "victim" -> victim.nameToJson,
      "neighbour" -> neighbour.nameToJson,
      "attackCards" -> attackCards.map(c => c.toJson),
      "blockedBy" -> blockedBy.map(entry => blockedByMapEntryToJson(entry))
    )
  }

  def blockedByMapEntryToJson(entry: (Card, Card)): JsObject = {
    Json.obj(
      "attackCards" -> entry._1.toJson,
      "blockingCards" -> entry._2.toJson
    )
  }

  override def toString: String = {
    ("Attacker: " + attacker.toString + "\n"
      + "Defender: " + victim.toString + "\n"
      + "Neighbor: " + neighbour.toString + "\n"
      + "Cards to block: " + attackCards.mkString("; ") + "\n"
      + "Blocked Cards: " + blockedBy.mkString("; "))
  }

}
