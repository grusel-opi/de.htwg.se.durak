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
      game = game.start()
      notifyObservers()
    }
  }

  def playCard(firstCard: Option[Card], secondCard: Option[Card]): Unit = {
    val res = game.playCard(firstCard, secondCard)
    game = res._2
    println("[CONTROLLER] res = " + res._1 + " :" + " play card: " + firstCard.toString)
    notifyObservers()
  }
  def playOK(): Unit = {
    game = game.continue
    notifyObservers()
  }
  def takeCards(): Unit = {
    game = game.takeCards()
    notifyObservers()
  }

}
