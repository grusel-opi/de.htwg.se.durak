package de.htwg.se.durak.model.gameComponent

import de.htwg.se.durak.model.cardComponent.cardBaseImpl.Card
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.{Game, Turn}
import de.htwg.se.durak.model.playerComponent.Player

import scala.xml.Elem

trait GameInterface {
  val players: List[Player]
  val deck: Deck
  val trump: Card
  val currentTurn: Turn
  val active: Player
  val winners: List[Player]

  def start(): Game
  def playOk(): Game
  def isShovable(card: Card): Boolean
  def closeTurn(success: Boolean): (Turn, Deck)
  def takeCards(): Game
  def distributeCards(people: List[Player]): Deck
  def playCard(card: Card, cardToBlock: Option[Card]): Game
  def defend(card: Card, cardToBlock: Option[Card]): Game
  def winByDefence(): Game
  def attack(card: Card): Game
  def shove(card: Card): Game
  def checkBlockCard(use: Card, against: Card): Boolean
  def checkAttackCard(card: Card): Boolean
  def nextPlayersMove(): Player
  def getNeighbour(player: Player): Player
  def getRightNeighbour(player: Player): Player
  def computePossibilities(): Either[List[Card], Map[Card, Card]]
  def computeAttackerPossibilities(): List[Card]
  def computeDefenderPossibilities(player: Option[Player]): Map[Card, Card]
}
