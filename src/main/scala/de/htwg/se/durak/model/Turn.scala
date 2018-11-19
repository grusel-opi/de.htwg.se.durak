package de.htwg.se.durak.model

case class Turn(attacker: Player, victim: Player, neighbor0: Player, neighbor1: Player, attackCards: List[Card], blockCards: List[Card], trumpCard: Card) {

//  def this(attacker: Player, victim: Player, neighbor0: Player, neighbor1: Player, attackCards: List[Card], trumpCard: Card)
//  = this(attacker: Player, victim: Player, neighbor0: Player, neighbor1: Player, attackCards: List[Card], List[Card](), trumpCard: Card)

  def addCard(player: Player, card: Card): Turn = player match {
    case `victim` => addBlockCard(card)
    case `attacker` => addAttackCard(card)
  }

  def addBlockCard(card: Card): Turn = {
    if (checkBlockCard(card)) {
      return Turn(attacker, victim, neighbor0, neighbor1, attackCards, card :: blockCards, trumpCard)
    }

    this
  }

  def addAttackCard(card: Card): Turn = {
    if (checkAttackCard(card)) {
      return Turn(attacker, victim, neighbor0, neighbor1, card :: attackCards, blockCards, trumpCard)
    }

    this
  }

  def checkAttackCard(card: Card): Boolean = {
    attackCards.size match {
      case 0 => return true
      case _ => attackCards.foreach(c => {
        if (c.value.compare(card.value) == 0) {
          return true
        }
      })
    }

    false
  }

  def checkBlockCard(card: Card): Boolean = {
    attackCards.foreach(c => {
      if (c.color == card.color && c.value.compare(card.value) == -1) {
        return true
      } else if (this.trumpCard.color != c.color && this.trumpCard.color == card.color) {
        return true
      }
    })
    false
  }

  override def toString: String = {
    val attackingCardsOnTableString: String = "Cards on table: " + attackCards + "\n"
    val blockingCardsString: String = "Blocking cards: " + blockCards + "\n"
    attackingCardsOnTableString.concat(blockingCardsString)
  }

}
