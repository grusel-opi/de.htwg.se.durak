package de.htwg.se.durak.controller

import de.htwg.se.durak.model._
import de.htwg.se.durak.util.Observable

class Controller(var game: DurakGame) extends Observable {

  var players: List[Player] = Nil

  def newPlayer(name: String): Unit = {
    players = Player(name, Nil)::players
    println("Added player " + name)
  }

  def newGame(): Unit = players.size match {
    case x if x < 2 => println("More players first!")
    case _          => {
      println("Ok: new game")
      game = new DurakGame(players)
      game.start()
      notifyObservers()
    }
  }

  def playCard(firstCard: Option[Card], secondCard: Option[Card]): Unit = {
    game.playCard(firstCard, secondCard)
    notifyObservers()
  }
  def playOK(): Unit = {
    game.nextMove()
    notifyObservers()
  }
  def takeCards(): Unit = {
    game.takeCards()
    notifyObservers()
  }

}
