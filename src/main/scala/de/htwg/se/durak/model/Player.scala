package de.htwg.se.durak.model

case class Player(name: String, var handCards: List[Card]) {
  def this(name: String) = this(name: String, Nil)

  def pickCards(cards: List[Card]): Unit = handCards = handCards ::: cards

  def dropCards(cards: List[Card]): Unit = handCards = handCards.filterNot(elem => cards.contains(elem))

  def hasCard(card: Card) : Boolean = handCards.contains(card)

  def sortHandCards(implicit ordering: Ordering[Card]): Unit = handCards = handCards.sorted

  override def toString: String = name
}
