package de.htwg.se.durak.model

case class Player(name: String, var next: Option[Player], var prev: Option[Player]) {
  var won: Boolean = false
  var handCards: List[Card] = List()

  def pickCard(deck: Deck): Unit = handCards = deck.popTopCard() :: handCards

  def removeCard(idx: Int): Unit = handCards = handCards.filterNot(elem => elem == handCards(idx))

  override def toString: String = name + ", handCards: " + handCards
}
