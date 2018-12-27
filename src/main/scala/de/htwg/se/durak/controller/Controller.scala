package de.htwg.se.durak.controller

import de.htwg.se.durak.model._
import de.htwg.se.durak.util.Observable

class Controller(var game: DurakGame) extends Observable {

  var players: List[Player] = Nil

  def newPlayer(name: String): Boolean = { // TODO: give ret val back to user (eg. when name is already in use or empty)
    if (!players.toStream.collect({case p => p.name}).contains(name) && name.nonEmpty) {
      players = Player(name, Nil)::players
      true
    } else {
      false
    }
  }

  def newGame(): Boolean = players.size match {
    case x if x < 2 => false
    case _          => {
      game = new DurakGame(players)
      game = game.start()
      notifyObservers()
      true
    }
  }

  def playCard(firstCard: Option[Card], secondCard: Option[Card]): Unit = {
    val res = game.playCard(firstCard, secondCard)
    game = res._2
    notifyObservers()
  }
  def playOK(): Unit = {
    game = game.playOk
    notifyObservers()
  }
  def takeCards(): Unit = {
    game = game.takeCards()
    notifyObservers()
  }

}
