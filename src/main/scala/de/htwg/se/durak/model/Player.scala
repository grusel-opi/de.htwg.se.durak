package de.htwg.se.durak.model

case class Player(name: String, handCards: List[Card]) {
  def this(name: String) = this(name: String, Nil)
  def pickCard(card: Card): Player = Player(name, card::handCards)
  def removeCard(card: Card): Player = Player(name, handCards.filterNot(elem => elem.equals(card)))
  override def toString: String = name + ", handCards: " + handCards
}
