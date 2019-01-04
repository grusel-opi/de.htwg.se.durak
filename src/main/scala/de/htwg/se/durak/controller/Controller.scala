package de.htwg.se.durak.controller

import de.htwg.se.durak.controller.events._
import de.htwg.se.durak.model._
import de.htwg.se.durak.util.{Observable, UndoManager}

import scala.swing.Publisher

class Controller(var game: DurakGame) extends Publisher {

  //TODO: give feedback to user about success of commands

  var players: List[Player] = Nil
  private val undoManager = new UndoManager()

  def newPlayer(name: String): Unit = {
    if (!players.toStream.collect({case p => p.name}).contains(name) && name.nonEmpty) {
      players = Player(name, Nil)::players
      publish(new NewPlayerEvent())
    } else {
      notifyUI("Player name already present!")
    }
  }

  def newGame(): Unit = {
    if (players.size > 1) {
      game = new DurakGame(players).start()
      publish(new NewGameEvent)
    } else {
      notifyUI("More than one player needed!")
    }
  }

  def playCard(firstCard: Option[Card], secondCard: Option[Card]): Unit = {
    if (firstCard.nonEmpty) {
      undoManager.doStep(new PlayCommand(firstCard, secondCard, this))
    } else {
      undoManager.purgeMemento()
      game = game.playCard(firstCard, secondCard)
    }
    publish(new CardsChanged)
    game = game.checkIfPlayerHasWon()
    val gameIsOver: Boolean = game.checkIfGameIsOver()
    if (gameIsOver) {
      publish(new GameOverEvent())
    }
  }

  def undo(): Unit = {
    undoManager.undoStep()
    publish(new CardsChanged)
  }

  def redo(): Unit = {
    undoManager.redoStep()
    publish(new CardsChanged)
  }

  def playOK(): Unit = {
    undoManager.purgeMemento()
    game = game.playOk
    publish(new CardsChanged)
  }
  def takeCards(): Unit = {
    undoManager.purgeMemento()
    val newGame = game.takeCards()
    if (newGame == game) {
      notifyUI("There are no cards to take.")
    } else {
      game = newGame
    }
    publish(new CardsChanged)
  }

  def notifyUI(message: String): Unit = {
    publish(new Notification(message))
    System.err.println(message)
  }

}
