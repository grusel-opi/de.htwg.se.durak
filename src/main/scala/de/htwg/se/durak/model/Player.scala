package de.htwg.se.durak.model

case class Player(name: String, next: Player, prev: Player, handCards: List[Card]) {
  def this(name: String, next: Player, prev: Player) = this(name: String, next: Player, prev: Player, Nil)
  def pickCard(card: Card): Player = Player(name, next, prev, card::handCards)
  def removeCard(card: Card): Player = Player(name, next, prev, handCards.filterNot(elem => elem.equals(card)))
  override def toString: String = name + ", handCards: " + handCards
}
