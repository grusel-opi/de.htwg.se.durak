package de.htwg.se.durak.model.deckComponent

import de.htwg.se.durak.model.cardComponent.cardBaseImpl.Card
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck

trait DeckInterface {
  def head: Card
  def tail: Deck
  def shuffle: Deck
  def popTopCard(): (Card, Deck)
  def popNCards(n: Int): (List[Card], Deck)
}
