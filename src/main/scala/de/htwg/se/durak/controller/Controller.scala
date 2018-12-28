package de.htwg.se.durak.controller

import de.htwg.se.durak.model._
import de.htwg.se.durak.util.{Observable, UndoManager}

class Controller(var game: DurakGame) extends Observable {

  //TODO: give feedback to user about success of commands

  var players: List[Player] = Nil
  private val undoManager = new UndoManager()

  def newPlayer(name: String): Unit = {
    if (!players.toStream.collect({case p => p.name}).contains(name) && name.nonEmpty) {
      players = Player(name, Nil)::players
    } else {
      System.err.println("Player name already present!\n")
    }
  }

  def newGame(): Unit = {
    if (players.size > 1) {
      game = new DurakGame(players).start()
      notifyObservers()
    } else {
      System.err.println("More than one player needed!\n")
    }
  }

  def playCard(firstCard: Option[Card], secondCard: Option[Card]): Unit = {
    if (firstCard.nonEmpty) {
      undoManager.doStep(new PlayCommand(firstCard, secondCard, this))
    } else {
      undoManager.purgeMemento()
      game = game.playCard(firstCard, secondCard)
    }
    notifyObservers()
  }

  def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers()
  }

  def playOK(): Unit = {
    undoManager.purgeMemento()
    game = game.playOk
    notifyObservers()
  }
  def takeCards(): Unit = {
    undoManager.purgeMemento()
    game = game.takeCards()
    notifyObservers()
  }

}
