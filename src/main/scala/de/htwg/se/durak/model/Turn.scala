package de.htwg.se.durak.model

case class Turn(attacker: Player, victim: Player, neighbor0: Player, neighbor1: Player, attackCards: List[Card], blockCards: List[Card]) {

  def this(attacker: Player, victim: Player, neighbor0: Player, neighbor1: Player, attackCards: List[Card])
  = this(attacker: Player, victim: Player, neighbor0: Player, neighbor1: Player, attackCards: List[Card], List[Card]())

  def addCard(player: Player, card: Card): Turn = player match {
    case `victim` => addBlockCard(card)
    case `attacker` => addAttackCard(card)
    case _ => this
  }

  def addBlockCard(card: Card): Turn = Turn(attacker, victim, neighbor0, neighbor1, attackCards, card :: blockCards)

  def addAttackCard(card: Card): Turn = if (checkCard(card)) {
    Turn(attacker, victim, neighbor0, neighbor1, card :: attackCards, blockCards)
  } else {
    this
  }

  def checkCard(card: Card): Boolean = {
    var res = false
    attackCards.foreach(c => if (c.value.equals(card.value)) res = true)
    res
  }

  override def toString: String = {
    //    val attackerString: String = "attacker: " + attacker + "\n"
    //    val victimString: String = "victim " + victim + "\n"
    //    val attackCardsString: String = "attackCards: " + attackCards + "\n"
    //    val blockCardsString: String = "blockCards: " + blockCards   + "\n"
    //    attackerString.concat(victimString).concat(attackCardsString).concat(blockCardsString)
    val attackingCardsOnTableString: String = "Cards on table: " + attackCards + "\n"
    val blockingCardsString: String = "Blocking cards: " + blockCards + "\n"
    attackingCardsOnTableString.concat(blockingCardsString)
  }

}
