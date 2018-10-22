package de.htwg.se.durak.model

case class Turn(victim: Player, attacker: Player, neighbor0: Player, neighbor1: Player, attackCards: Set[Card], blockCards: Set[Card]) {

  def this(victim: Player, attacker: Player, neighbor0: Player, neighbor1: Player, attackCards: Set[Card])
  = this(victim: Player, attacker: Player, neighbor0: Player, neighbor1: Player, attackCards: Set[Card], Nil)

  def addCard(player: Player, card: Card): Turn = {
    player match {
      case victim =>
    }

    if (player.equals(victim)) {
      addBlockCard(card)
    } else if (player.equals(attacker)) {
      addAttackCard(card)
    } else {
      this
    }

  }

  def addBlockCard(card: Card): Turn = this(victim, attacker, neighbor0, neighbor1, attackCards, blockCards + card)

  def addAttackCard(card: Card): Turn = {
    if (checkCard(card)) {
      this(victim, attacker, neighbor0, neighbor1, attackCards + card, blockCards)
    } else {
      this
    }
  }

  def checkCard(card: Card) : Boolean = {
    var res = false
    attackCards.foreach(c => if (c.value.equals(card.value)) res = true)
    res
  }

}
