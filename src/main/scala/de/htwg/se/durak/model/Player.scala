package de.htwg.se.durak.model

case class Player(name: String, handCards: List[Card]) {
  def this(name: String) = this(name: String, Nil)

  def pickCards(cards: List[Card]): Player = copy(handCards = cards:::handCards)

  def dropCards(cards: List[Card]): Player = copy(handCards = handCards.filterNot(elem => cards.contains(elem)))

  def hasCard(card: Card) : Boolean = handCards.contains(card)

  override def toString: String = name
}
