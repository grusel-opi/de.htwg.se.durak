package de.htwg.se.durak.controller.controller

import java.io.FileNotFoundException

import de.htwg.se.durak.Durak.injector
import de.htwg.se.durak.controller.controllerComponent.{ControllerInterface, GameStatus}
import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.durak.model.turnComponent.turnBaseImpl.Turn
import de.htwg.se.durak.controller.controllerComponent.ExceptionEvent
import de.htwg.se.durak.util.customExceptions.IllegalTurnException
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner
import org.testfx.api.FxToolkit

import scala.io.{BufferedSource, Source}
import util.control.Breaks._


@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {
  val timeToSleep = 100

  "A ExceptionEvent" when {
    "trying to get an exception" should {
      "return an exception" in {
        val iTE = new ExceptionEvent(new IllegalTurnException())
        val ex = iTE.getException

        ex.getClass should be(classOf[IllegalTurnException])
        ex.getMessage should be("Illegal Turn!")
      }
    }
  }

  "A controller" when {
    FxToolkit.registerPrimaryStage()
    val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

    "created" should {
      "have an empty player list" in {
        controller.players should be(List.empty)
      }

      "have an default game" in {
        val deck = controller.game.deck
        val trump_card = controller.game.trump
        val default_player = new Player("default")
        val default_turn = new Turn(default_player, default_player, default_player)

        controller.game.deck should be(deck)
        controller.game.players should be(List(default_player))
        controller.game.active should be(default_player)
        controller.game.trump should be(trump_card)
        controller.game.currentTurn should be(default_turn)
        controller.game.winners should be(List.empty)

        controller.gameStatus should be(GameStatus.IDLE)
        GameStatus.message(controller.gameStatus) should be("")
      }
    }

    "adding a new player" should {
      "have a new player in the players list" in {
        controller.newPlayer("Hans")

        while (controller.players.isEmpty) {
          Thread.sleep(timeToSleep)
        }

        controller.players.size should be(1)
        controller.players.head.name should be("Hans")
        controller.players.head.handCards should be(List.empty)
        controller.gameStatus should be(GameStatus.NEWPLAYER)
        GameStatus.message(controller.gameStatus) should be("A new player was added")

      }

      "set the game status to 'PLAYERALREADYPRESENT', if the player already exists" in {
        controller.newPlayer("Hans")

        while (controller.gameStatus == GameStatus.NEWPLAYER) {
          Thread.sleep(timeToSleep)
        }

        controller.gameStatus should be(GameStatus.PLAYERALREADYPRESENT)
        GameStatus.message(controller.gameStatus) should be("The player you want to add already exists.")
      }
    }

    "reseting the players list" should {
      "have an empty player list" in {
        controller.resetPlayers()

        while (controller.players.nonEmpty) {
          Thread.sleep(timeToSleep)
        }

        controller.players should be(List.empty)
        controller.gameStatus should be(GameStatus.RESETPLAYERS)
        GameStatus.message(controller.gameStatus) should be("Reset the players list.")
      }
    }

    "creating a new game with too few players " should {
      "set the game status to 'MOREPLAYERSNEEDED'" in {
        controller.newGame()

        while (controller.gameStatus == GameStatus.RESETPLAYERS) {
          Thread.sleep(timeToSleep)
        }

        controller.gameStatus should be(GameStatus.MOREPLAYERSNEEDED)
        GameStatus.message(controller.gameStatus) should be("More players required to start the game.")
      }
    }

    "creating a new game with enough players" should {
      "create a new game" in {
        var player1 = new Player("Hans")
        var player2 = new Player("Peter")

        controller.newPlayer(player1.name)
        controller.newPlayer(player2.name)

        while (controller.players.size != 2) {
          Thread.sleep(timeToSleep)
        }

        controller.newGame()

        while (controller.gameStatus == GameStatus.NEWPLAYER) {
          Thread.sleep(timeToSleep)
        }

        controller.gameStatus should be(GameStatus.NEW)
        GameStatus.message(controller.gameStatus) should be("A new game was created.")

        controller.players.size should be(2)
        controller.players.foreach(player => {
          player.name should (be(player1.name) or be(player2.name))
        })

        controller.game.winners should be(List.empty)
        controller.game.active.name should (be(player1.name) or be(player2.name))

        val deck = controller.game.deck
        val trump_card = controller.game.trump
        val attacker = controller.game.active
        val victim = controller.game.getNeighbour(controller.game.active)
        val turn = Turn(attacker, victim, controller.game.getNeighbour(victim), List.empty, Map.empty)

        controller.game.currentTurn should be(turn)
        controller.game.deck should be(deck)
        controller.game.trump should be(trump_card)
      }
    }

    var last_controller_state: Option[GameInterface] = None

    "playing a card" should {
      "add a attack card, if the attacker lays a card" in {
        val card_to_lay = controller.game.active.handCards.head
        last_controller_state = Some(controller.game)
        controller.playCard(controller.game.active.handCards.head, None)

        while (controller.gameStatus == GameStatus.NEW && controller.game.currentTurn.attackCards.isEmpty) {
          Thread.sleep(timeToSleep)
        }

        controller.game.currentTurn.attackCards should be(List(card_to_lay))
        controller.game.currentTurn.blockedBy should be(Map.empty)
        controller.gameStatus should be(GameStatus.CARDLAYED)
        GameStatus.message(controller.gameStatus) should be("A card was layed.")
      }
    }

    var new_controller_state: Option[GameInterface] = None

    "making an undo" should {
      "set the game status to 'UNDO' and should undo the last step" in {
        while (last_controller_state.isEmpty) {
          Thread.sleep(timeToSleep)
        }

        new_controller_state = Some(controller.game)
        controller.undo()

        while (controller.game.currentTurn.attackCards.nonEmpty) {
          Thread.sleep(timeToSleep)
        }

        controller.game should be(last_controller_state.get)
        controller.gameStatus should be(GameStatus.UNDO)
        GameStatus.message(controller.gameStatus) should be("Undone one step.")
      }
    }

    "making a redo" should {
      "sets the game status to 'REDO' and should redo the last step" in {
        while (new_controller_state.isEmpty) {
          Thread.sleep(timeToSleep)
        }

        controller.redo()

        while (controller.game.currentTurn.attackCards.isEmpty && controller.gameStatus == GameStatus.UNDO) {
          Thread.sleep(timeToSleep)
        }

        controller.game should be(new_controller_state.get)
        controller.gameStatus should be(GameStatus.REDO)
        GameStatus.message(controller.gameStatus) should be("Redone one step.")
      }
    }

    "playing ok" should {
      "set the victim as active player, if the attacker layed a valid card" in {
        controller.playOk()

        while (controller.gameStatus == GameStatus.REDO) {
          Thread.sleep(timeToSleep)
        }

        controller.game.active should be(controller.game.currentTurn.victim)
        controller.gameStatus should be(GameStatus.OK)
        GameStatus.message(controller.gameStatus) should be("Player is ok.")

      }
    }

    "playing an illegal turn " should {
      "set the game status to 'ILLEGALTURN'" in {
        var lowerCard: Option[CardInterface] = None

        breakable {
          while (true) {
            controller.game.deck.cards.foreach(card => {
              if (card.color != controller.game.currentTurn.attackCards.head.color && card.color != controller.game.trump.color) {
                lowerCard = Some(card)
                break
              } else if (card.color == controller.game.trump.color && card.color == controller.game.currentTurn.attackCards.head.color
                && card.value < controller.game.currentTurn.attackCards.head.value) {
                lowerCard = Some(card)
                break
              } else if (card.color == controller.game.currentTurn.attackCards.head.color && card.value < controller.game.currentTurn.attackCards.head.value) {
                lowerCard = Some(card)
                break
              }
            })
            break
          }
        }

        controller.game.currentTurn.victim.pickCards(List(lowerCard.get))

        val lastGameState = controller.game

        while (controller.game.currentTurn.victim.handCards.size < 6) {
          Thread.sleep(timeToSleep)
        }

        controller.playCard(lowerCard.get, Some(controller.game.currentTurn.attackCards.head))

        while (controller.gameStatus != GameStatus.ILLEGALTURN) {
          Thread.sleep(timeToSleep)
        }

        controller.gameStatus should be(GameStatus.ILLEGALTURN)
        GameStatus.message(controller.gameStatus) should be("Illegal turn.")
        controller.game should be(lastGameState)
      }
    }

    "a player take the cards" should {
      "start a new turn" in {
        val new_active_player = controller.game.currentTurn.attacker

        controller.takeCards()

        while (controller.gameStatus != GameStatus.TAKE) {
          Thread.sleep(timeToSleep)
        }

        controller.gameStatus should be(GameStatus.TAKE)
        GameStatus.message(controller.gameStatus) should be ("Player take cards")

        controller.game.active should be(new_active_player)
        controller.game.currentTurn.attacker should be(new_active_player)
        controller.game.currentTurn.victim should be(controller.game.getNeighbour(new_active_player))
        controller.game.currentTurn.attackCards should be(List.empty)
        controller.game.currentTurn.blockedBy should be(Map.empty)
        controller.game.currentTurn.neighbour should be(controller.game.getNeighbour(controller.game.currentTurn.victim))
      }
    }

    "a attacker is trying to play ok, but layed no cards already" should {
      "set the game status to 'LAYCARDFIRST'" in {
        controller.playOk()

        while (controller.gameStatus != GameStatus.LAYCARDFIRST) {
          Thread.sleep(timeToSleep)
        }

        controller.gameStatus should be(GameStatus.LAYCARDFIRST)
        GameStatus.message(controller.gameStatus) should be("You have to lay a card first.")
      }
    }

    "a attacker is trying to take the cards, but no cards were layed already" should {
      "set the game status to 'LAYCARDFIRST'" in {
        controller.takeCards()

        while (controller.gameStatus != GameStatus.NOCARDSTOTAKE) {
          Thread.sleep(timeToSleep)
        }

        controller.gameStatus should be(GameStatus.NOCARDSTOTAKE)
        GameStatus.message(controller.gameStatus) should be("There are no cards to take.")
      }
    }

    "checking if the game is over, and the player size is only 1" should {
      "set the game status to 'OVER" in {
        // TODO
      }
    }

    "trying to get the active player as string" should {
      "return the active player as string" in {
        controller.activePlayerToString() should be(controller.game.active.toString)
      }
    }

    "trying to get the handcards of the active player as string" should {
      "return the handcards of the active player as string" in {
        controller.activePlayerHandCardsToString() should be(controller.game.active.handCards.mkString(","))
      }
    }

    "trying to get the trump card as string" should {
      "return the trump card as string" in {
        controller.trumpCardToString() should be(controller.game.trump.toString)
      }
    }

    "trying to get the current turn as string" should {
      "return the current turn as string" in {
        controller.currentTurnToString() should be(controller.game.currentTurn.toString)
      }
    }

    "trying to get the current attacker as string" should {
      "return the current attacker as string" in {
        controller.currentAttackerToString() should be(controller.game.currentTurn.attacker.toString)
      }
    }

    "trying to get the current victim as string" should {
      "return the current victim as string" in {
        controller.currentVictimToString() should be(controller.game.currentTurn.victim.toString)
      }
    }

    "trying to get the current neighbour as string" should {
      "return the current neighbour as string" in {
        controller.currentNeighbourToString() should be(controller.game.currentTurn.neighbour.toString)
      }
    }

    "trying to get the current attack cards as string" should {
      "return the current attack cards as string" in {
        controller.currentAttackCardsToString() should be(controller.game.currentTurn.attackCards.mkString(","))
      }
    }

    "trying to get the current blocked by map as string" should {
      "return the current blocked by map as string" in {
        controller.getCurrentBlockedByMap should be(controller.game.currentTurn.blockedBy)
      }
    }

    "trying to get the deck size as string" should {
      "return the deck size as string" in {
        controller.deckSizeToString() should be(controller.game.deck.cards.size.toString)
      }
    }

    "trying to get winners as string" should {
      "return the winners as string" in {
        controller.winnersToString() should be(controller.game.winners.toString())
      }
    }

    "trying to exit the game" should {
      "set the game status to 'EXIT' and exit the whole program" in {
        controller.exitGame()

        while (controller.gameStatus != GameStatus.EXIT) {
          Thread.sleep(timeToSleep)
        }

         controller.gameStatus should be(GameStatus.EXIT)
      }
    }

    "saving a game" should {
      "produce a save file with the given filename and set the game status to 'SAVED'" in {
        val file_name_with_extension = "controller_test.json"

        controller.saveGame(file_name_with_extension)

        while (controller.gameStatus != GameStatus.SAVED) {
          Thread.sleep(timeToSleep)
        }

        val location = "save/" + file_name_with_extension

        var file: Option[BufferedSource] = None

        try {
          file = Some(Source.fromFile(location))
        } catch {
          case fnfe: FileNotFoundException => file = None
        }

        controller.gameStatus should be(GameStatus.SAVED)
        GameStatus.message(controller.gameStatus) should be("The game was saved.")
        file should not be None
      }
    }
  }
}
