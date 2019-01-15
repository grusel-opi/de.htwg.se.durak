package de.htwg.se.durak.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject}
import de.htwg.se.durak.DurakModule
import de.htwg.se.durak.controller.controllerComponent._
import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.model.fileIOComponent.FileIOInterface
// import de.htwg.se.durak.model.fileIOComponent.fileIOXmlImpl.FileIO
import de.htwg.se.durak.model.fileIOComponent.fileIOJsonImpl.FileIO
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.util.customExceptions._
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.{Game, PlayCommand}
import de.htwg.se.durak.model.playerComponent.Player
import de.htwg.se.durak.util.undoManager.UndoManager

import scala.swing.Publisher

class Controller @Inject() (var game: GameInterface) extends ControllerInterface with Publisher {

  var players: List[Player] = Nil
  private val undoManager = new UndoManager
  val fileIO = new FileIO
  // val injector = Guice.createInjector(new DurakModule)

  def newPlayer(name: String): Unit = {
    if (!players.toStream.collect({ case p => p.name }).contains(name) && name.nonEmpty) {
      players = Player(name, Nil) :: players
      publish(new NewPlayerEvent)
    } else {
      notifyUI(new PlayerAlreadyPresentException)
    }
  }

  def resetPlayers(): Unit = {
    players = Nil
    publish(new ResetPlayersEvent)
  }


  def newGame(): Unit = {
    if (players.size > 1) {
      game = new Game(players).start()
      publish(new NewGameEvent)
    } else {
      notifyUI(new MoreThanOnePlayerNeededException)
    }
  }

  def playCard(firstCard: Card, secondCard: Option[Card]): Unit = {
    try {
      undoManager.doStep(new PlayCommand(firstCard, secondCard, this))

      if (!checkIfGameIsOver) {
        publish(new CardsChangedEvent)
      }
    }
    catch {
      case iTE: IllegalTurnException => notifyUI(iTE)
      case mBCE: MissingBlockingCardException => notifyUI(mBCE)
      case vHNECTBE: VictimHasNotEnoughCardsToBlockException => notifyUI(vHNECTBE)
    }
  }

  def throwCardIn(card: Card): Unit = {
    try {
      undoManager.doStep(new PlayCommand(card, None, this))
      if (!checkIfGameIsOver) {
        publish(new CardsChangedEvent)
      }
    } catch {
      case vHNECTBE: VictimHasNotEnoughCardsToBlockException => notifyUI(vHNECTBE)
      case iTE: IllegalTurnException => notifyUI(iTE)
    }

  }

  def undo(): Unit = {
    undoManager.undoStep()
    publish(new CardsChangedEvent)
  }


  def redo(): Unit = {
    undoManager.redoStep()
    publish(new CardsChangedEvent)
  }

  def playOk(): Unit = {
    undoManager.purgeMemento()
    try {
      game = game.playOk()
      publish(new CardsChangedEvent)
    } catch {
      case _ : LayCardFirsException => notifyUI(new LayCardFirsException)
    }
  }

  def takeCards(): Unit = {
    try {
      undoManager.purgeMemento()
      game = game.takeCards()
      publish(new CardsChangedEvent)
    } catch {
      case nCTTE: NoCardsToTakeException => notifyUI(nCTTE)
    }
  }

  def checkIfGameIsOver: Boolean = {
    if (game.players.size == 1) {
      publish(new GameOverEvent)
      true
    } else {
      false
    }
  }

  def notifyUI(exception: Exception): Unit = {
    publish(new ExceptionEvent(exception))
    System.err.println(exception.getMessage)
  }

  def activePlayerToString(): String = {
    game.active.toString
  }

  def activePlayerHandCardsToString(): String = {
    game.active.handCards.mkString(",")
  }

  def trumpCardToString(): String = {
    game.trump.toString()
  }

  def currentTurnToString(): String = {
    game.currentTurn.toString
  }

//  def playersOkToString(): String = {
//    game.ok.mkString(", ")
//  }

  def currentAttackerToString(): String = {
    game.currentTurn.attacker.toString
  }

  def currentVictimToString(): String = {
    game.currentTurn.victim.toString
  }

  def currentNeighbourToString(): String = {
    game.currentTurn.neighbour.toString
  }

  def currentAttackCardsToString(): String = {
    game.currentTurn.attackCards.mkString(",")
  }

 def getCurrentBlockedByMap(): Map[Card, Card] = {
   game.currentTurn.blockedBy
 }

  def deckSizeToString(): String = {
    game.deck.cards.size.toString
  }

  def winnerToString(): String = {
    game.winners.toString
  }

  def saveGame(fileName: String): Unit = {
    fileIO.save(game, fileName)
  }

  def loadGame(fileName: String): Unit = {
    game = fileIO.load(fileName)
    publish(new NewGameEvent)
  }
}
