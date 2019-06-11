package de.htwg.se.durak.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.durak.DurakModule
import de.htwg.se.durak.controller.controllerComponent.GameStatus._
import de.htwg.se.durak.controller.controllerComponent._
import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.fileIOComponent._
import scalafx.application.Platform
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.util.customExceptions._
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.Game
import de.htwg.se.durak.model.playerComponent.PlayerInterface
import de.htwg.se.durak.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.durak.util.PlayCommand
import de.htwg.se.durak.util.undoManager.UndoManager

import scala.swing.Publisher

class Controller @Inject()(var game: GameInterface) extends ControllerInterface with Publisher {

  var gameStatus: GameStatus = IDLE
  var players: List[PlayerInterface] = Nil
  private val undoManager = new UndoManager
  val injector: ScalaInjector = Guice.createInjector(new DurakModule)
  val fileIO: FileIOInterface = injector.instance[FileIOInterface]

  def newPlayer(name: String): Unit = {
    Platform.runLater(() => {
      if (!players.toStream.collect({ case p => p.name }).contains(name) && name.nonEmpty) {
        players = Player(name, Nil) :: players
        gameStatus = NEWPLAYER
        publish(new NewPlayerEvent)
      } else {
        gameStatus = PLAYERALREADYPRESENT
        notifyUI(new PlayerAlreadyPresentException)
      }
    })
  }

  def resetPlayers(): Unit = {
    Platform.runLater(() => {
      players = Nil
      gameStatus = RESETPLAYERS
      publish(new ResetPlayersEvent)
    })
  }


  def newGame(): Unit = {
    Platform.runLater(() => {
      if (players.size > 1) {
        game = new Game(players).start()
        gameStatus = NEW
        publish(new NewGameEvent)
      } else {
        gameStatus = MOREPLAYERSNEEDED
        notifyUI(new MoreThanOnePlayerNeededException)
      }
    })
  }

  def playCard(firstCard: CardInterface, secondCard: Option[CardInterface]): Unit = {
    Platform.runLater(() => {
      try {
        undoManager.doStep(new PlayCommand(firstCard, secondCard, this))
        if (!checkIfGameIsOver) {
          gameStatus = CARDLAYED
          publish(new CardsChangedEvent)
        }
      }
      catch {
        case iTE: IllegalTurnException =>
          gameStatus = ILLEGALTURN
          notifyUI(iTE)
        case mBCE: MissingBlockingCardException =>
          gameStatus = MISSINGBLOCKINGCARD
          notifyUI(mBCE)
        case vHNECTBE: VictimHasNotEnoughCardsToBlockException =>
          gameStatus = VICTIMHASNOTENOUGHTCARDS
          notifyUI(vHNECTBE)
      }
    })
  }

  def throwCardIn(card: CardInterface): Unit = {
    Platform.runLater(() => {
      try {
        undoManager.doStep(new PlayCommand(card, None, this))
        if (!checkIfGameIsOver) {
          gameStatus = CARDLAYED
          publish(new CardsChangedEvent)
        }
      } catch {
        case vHNECTBE: VictimHasNotEnoughCardsToBlockException =>
          gameStatus = VICTIMHASNOTENOUGHTCARDS
          notifyUI(vHNECTBE)
        case iTE: IllegalTurnException =>
          gameStatus = ILLEGALTURN
          notifyUI(iTE)
      }
    })
  }

  def undo(): Unit = {
    Platform.runLater(() => {
      undoManager.undoStep()
      gameStatus = UNDO
      publish(new CardsChangedEvent)
    })
  }

  def redo(): Unit = {
    Platform.runLater(() => {
      undoManager.redoStep()
      gameStatus = REDO
      publish(new CardsChangedEvent)
    })
  }

  def playOk(): Unit = {
    Platform.runLater(() => {
      undoManager.purgeMemento()
      try {
        game = game.playOk()
        gameStatus = OK
        publish(new CardsChangedEvent)
      } catch {
        case _: LayCardFirsException =>
          gameStatus = LAYCARDFIRST
          notifyUI(new LayCardFirsException)
      }
    })
  }

  def takeCards(): Unit = {
    Platform.runLater(() => {
      try {
        undoManager.purgeMemento()
        game = game.takeCards()
        gameStatus = TAKE
        publish(new CardsChangedEvent)
      } catch {
        case nCTTE: NoCardsToTakeException =>
          gameStatus = NOCARDSTOTAKE
          notifyUI(nCTTE)
      }
    })
  }

  def checkIfGameIsOver: Boolean = {
    if (game.players.size == 1) {
      Platform.runLater(() => {
        gameStatus = OVER
        publish(new GameOverEvent)
      })
      true
    } else {
      false
    }
  }

  def notifyUI(exception: Exception): Unit = {
    Platform.runLater(() => {
      publish(new ExceptionEvent(exception))
    })
  }

  def activePlayerToString(): String = {
    game.active.toString
  }

  def activePlayerHandCardsToString(): String = {
    game.active.handCards.mkString(",")
  }

  def trumpCardToString(): String = {
    game.trump.toString
  }

  def currentTurnToString(): String = {
    game.currentTurn.toString
  }

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

  def getCurrentBlockedByMap: Map[CardInterface, CardInterface] = {
    game.currentTurn.blockedBy
  }

  def deckSizeToString(): String = {
    game.deck.cards.size.toString
  }

  def winnerToString(): String = {
    game.winners.toString()
  }

  def saveGame(fileName: String): Unit = {
    gameStatus = SAVED
    fileIO.save(game, fileName)
  }

  def loadGame(fileName: String): Unit = {
    Platform.runLater(() => {
      game = fileIO.load(fileName)
      gameStatus = LOADED
      publish(new NewGameEvent)
    })
  }

  def exitGame(): Unit = {
    System.exit(0)
  }

}
