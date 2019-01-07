package de.htwg.se.durak.model

case class Turn(attacker: Player, victim: Player, neighbor: Player, attackCards: List[Card], blockedBy: Map[Card, Card]) {

  def this(attacker: Player, victim: Player, neighbor: Player)
  = this(attacker: Player, victim: Player, neighbor: Player, Nil, Map())

  def addBlockCard(attackCard: Card, blockCard: Card): Turn = copy(attacker, victim, neighbor,
    attackCards.filterNot(c => c.equals(attackCard)), blockedBy + (attackCard -> blockCard))

  def addAttackCard(card: Card): Turn = copy(attacker, victim, neighbor, attackCards ::: List(card))

  def getCards: List[Card] = attackCards ::: blockedBy.values.toList ::: blockedBy.keys.toList

  def getPlayers: List[Player] = List(attacker, victim, neighbor)

  override def toString: String = {
    ("Attacker: " + attacker.toString + "\n"
      + "Defender: " + victim.toString + "\n"
      + "Neighbor: " + neighbor.toString + "\n"
      + "Cards to block: " + attackCards.mkString("; ") + "\n"
      + "Blocked Cards: " + blockedBy.mkString("; "))
  }

}
