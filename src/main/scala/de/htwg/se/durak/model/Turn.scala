package de.htwg.se.durak.model

case class Turn(attacker: Player, victim: Player, neighbor: Player, attackCards: List[Card], blockedBy: Map[Card, Card]) {

  def this(attacker: Player, victim: Player, neighbor: Player)
  = this(attacker: Player, victim: Player, neighbor: Player, List[Card](), Map[Card, Card]())

  def addBlockCard(attackCard: Card, blockCard: Card): Turn = {
    copy(attacker, victim, neighbor, attackCards.filterNot(c => c.equals(attackCard)), blockedBy + (attackCard -> blockCard))
  }

  def addAttackCard(card: Card): Turn = copy(attacker, victim, neighbor, card::attackCards)

  override def toString: String = {
    //TODO: pretty print
    "turn"
  }

}
