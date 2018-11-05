package de.htwg.se.durak.model

case class Player(name: String, handCards: List[Card]) {
  def this(name: String) = this(name: String, Nil)

  def pickCard(card: Card): Player = copy(handCards=card::handCards)

  def pickCards(cards: List[Card]): Player = Player(name, cards ::: handCards)

  def removeCard(card: Card): Player = Player(name, handCards.filterNot(elem => elem.equals(card)))

  def removeCards(cards: List[Card]): Player = Player(name, handCards.filter(!cards.contains(_)))

  override def toString: String = name
}
