package de.htwg.se.durak.model

case class Turn(victim: Player, attacker: Player, neighbor0: Player, neighbor1: Player, attackCards: Set[Card]) {

  var blockCards: Set[Card] = Set()

  def addBlockCard(card: Card): Unit = {
    blockCards += card
  }

  def addAttackCard(card: Card): Option[Turn] = {
    if (checkCard(card)) {
      Some(Turn(victim, attacker, neighbor0, neighbor1, attackCards + card))
    } else {
      None
    }
  }

  def checkCard(card: Card) : Boolean = {
    var res = false
    attackCards.foreach(c => if (c.value.equals(card.value)) res = true)
    res
  }

}
