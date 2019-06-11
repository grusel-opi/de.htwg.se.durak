package de.htwg.se.durak.controller.controller

import de.htwg.se.durak.Durak.injector
import de.htwg.se.durak.controller.controllerComponent.{ControllerInterface, GameStatus}
import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.durak.model.turnComponent.turnBaseImpl.Turn
import de.htwg.se.durak.util.customExceptions.IllegalTurnException
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner
import org.testfx.api.FxToolkit

import util.control.Breaks._


@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {
  val timeToSleep = 100

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

        while (controller.gameStatus == GameStatus.NEW) {
          Thread.sleep(timeToSleep)
        }

        controller.gameStatus should be(GameStatus.CARDLAYED)

        while (controller.game.currentTurn.attackCards.isEmpty) {
          Thread.sleep(timeToSleep)
        }

        controller.game.currentTurn.attackCards should be(List(card_to_lay))
        controller.game.currentTurn.blockedBy should be(Map.empty)
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
      }
    }

    "making a redo" should {
      "sets the game status to 'REDO' and should redo the last step" in {
        while (new_controller_state.isEmpty) {
          Thread.sleep(timeToSleep)
        }

        controller.redo()

        while (controller.game.currentTurn.attackCards.isEmpty) {
          Thread.sleep(timeToSleep)
        }

        controller.game should be(new_controller_state.get)
      }
    }

    "playing an illegal turn " should {
      "thow an IllegalTurnException" in {
        println(controller.game)
        var lowerCard: Option[CardInterface] = None

        breakable {
          while (true) {
            controller.game.deck.cards.foreach(card => {
              if (card.color != controller.game.currentTurn.attackCards.head.color && card.value < controller.game.currentTurn.attackCards.head.value) {
                lowerCard = Some(card)
                break
              } else if (card.color == controller.game.trump.color && card.value < controller.game.currentTurn.attackCards.head.value) {
                lowerCard = Some(card)
                break
              }
            })
            break
          }

        }

        try {
          controller.playCard(controller.game.currentTurn.attackCards.head, lowerCard)
        } catch {
          case ite: IllegalTurnException => true
        }

        false
      }
    }
  }

}
