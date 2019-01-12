package de.htwg.se.durak.model.gameComponent

import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.{Game, Turn}
import de.htwg.se.durak.model.playerComponent.Player

import scala.xml.Elem

trait GameInterface {
  def start(): Game
  def playOk(): Game
  def continue(): Game
  def closeTurn(success: Boolean): (Turn, Deck)
  def takeCards(): Game
  def playCard(card: Option[Card], cardToBlock: Option[Card]): Game
  def defend(card: Card, cardToBlock: Option[Card]): Game
  def defendAndSetWinner(): Game
  def attack(card: Card): Game
  def shove(card: Card): Game
  def checkBlockCard(use: Card, against: Card): Boolean
  def checkAttackCard(card: Card): Boolean
  def nextPlayersMove(): Player
  def getNeighbour(player: Player): Player
  def computePossibilities(): Either[List[Card], Map[Card, Card]]
  def computeAttackerPossibilities(): List[Card]
  def computeDefenderPossibilities(player: Option[Player]): Map[Card, Card]
  def toXml: Elem
}
