package de.htwg.se.durak.model.gameComponent.gameBaseImpl

import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.model.playerComponent.Player

case class Turn(attacker: Player, victim: Player, neighbour: Player, attackCards: List[Card], blockedBy: Map[Card, Card]) {

  def this(attacker: Player, victim: Player, neighbor: Player)
  = this(attacker: Player, victim: Player, neighbor: Player, Nil, Map())

  def addBlockCard(attackCard: Card, blockCard: Card): Turn = copy(attacker, victim, neighbour,
    attackCards.filterNot(c => c.equals(attackCard)), blockedBy + (attackCard -> blockCard))

  def addAttackCard(card: Card): Turn = copy(attacker, victim, neighbour, attackCards ::: List(card))

  def getCards: List[Card] = attackCards ::: blockedBy.values.toList ::: blockedBy.keys.toList

  def getPlayers: List[Player] = List(attacker, victim, neighbour)

  override def toString: String = {
    ("Attacker: " + attacker.toString + "\n"
      + "Defender: " + victim.toString + "\n"
      + "Neighbor: " + neighbour.toString + "\n"
      + "Cards to block: " + attackCards.mkString("; ") + "\n"
      + "Blocked Cards: " + blockedBy.mkString("; "))
  }

}
