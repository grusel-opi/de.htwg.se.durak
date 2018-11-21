package de.htwg.se.durak.controller

import de.htwg.se.durak.model._
import de.htwg.se.durak.util.Observable

class Controller(var game: DurakGame) extends Observable {

  var players: List[Player] = Nil

  def newPlayer(name: String): Unit = players = Player(name, Nil)::players

  def newGame(): Unit = players.size match {
    case x if x < 2 => // logging later
    case _          => game = new DurakGame(players)
  }

  def playCard(firstCard: Option[Card], secondCard: Option[Card]): Unit = game.playCard(firstCard, secondCard)

  def playOK(): Unit = game.nextMove()
  def takeCards(): Unit = game.takeCards()

}
