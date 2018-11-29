package de.htwg.se.durak.model

case class Turn(attacker: Player, victim: Player, neighbor: Player, attackCards: List[Card], blockedBy: Map[Card, Card]) {

  def this(attacker: Player, victim: Player, neighbor: Player)
  = this(attacker: Player, victim: Player, neighbor: Player, List[Card](), Map[Card, Card]())

  def addBlockCard(attackCard: Card, blockCard: Card): Turn = {
    copy(attacker, victim, neighbor, attackCards.filterNot(c => c.equals(attackCard)), blockedBy + (attackCard -> blockCard))
  }

  def getAllCards: List[Card] = attackCards ::: blockedBy.values.toList ::: blockedBy.keys.toList
  
  def addAttackCard(card: Card): Turn = copy(attacker, victim, neighbor, card :: attackCards)

  override def toString: String = {
    ("Attacker: " + attacker.toString + "\n"
      + "Defender: " + victim.toString + "\n"
      + "Neighbor: " + neighbor.toString + "\n"
      + "Cards to block: \n" + attackCards.mkString("; ") + "\n"
      + "Blocked Cards: ") + blockedBy.mkString("; ") + "\n"
  }

}
