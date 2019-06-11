package de.htwg.se.durak.controller.controller

import com.sun.javafx.application.PlatformImpl
import de.htwg.se.durak.Durak.injector
import de.htwg.se.durak.controller.controllerComponent.{ControllerInterface, GameStatus}
import de.htwg.se.durak.model.playerComponent.playerBaseImpl.Player
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {
  val timeToSleep = 100

  "A controller" when {
    PlatformImpl.startup(() => {})
    val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

    "created" should {
      "have a IDLE game status" in {
        controller.gameStatus should be(GameStatus.IDLE)
      }

      "have an empty player list" in {
        controller.players should be(List.empty)
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
      }
    }

    "reset the players list" should {
      "have an empty player list" in {
        controller.resetPlayers()

        while (controller.players.nonEmpty) {
          Thread.sleep(timeToSleep)
        }

        controller.players should be (List.empty)
      }
    }
  }
}
