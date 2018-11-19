package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest.PrivateMethodTester.PrivateMethod
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DeskSpec extends WordSpec with Matchers {

  val player1: Player = new Player("Hannes")
  val player2: Player = new Player("Abdul")
  val player3: Player = new Player("Wilma")
  val playerList: List[Player] = List(player1, player2, player3)

  val deck: Deck = new Deck().shuffle

  val trump: Card = deck.getLastCard

  val currentTurn: Option[Turn] = None

  val durakGame: Desk = Desk(playerList, deck, trump, currentTurn)

  "A Desk" when {
    "created" should {
      "have a player list." in {
        durakGame.players should be(playerList)
      }

      "have a deck." in {
        durakGame.deck should be(deck)
      }

      "have a trump card." in {
        durakGame.trump should be(trump)
      }

      "have a current turn option." in {
        durakGame.currentTurn should be(None)
      }
    }

    "trying to get neighbors of a player" should {
      "return the neigbours of the choosen player." in {
        val neighborsOfPlayer1: (Player, Player) = (player2, player3)
        val neighborsOfPlayer2: (Player, Player) = (player1, player3)
        val neighborsOfPlayer3: (Player, Player) = (player2, player1)

        durakGame.getNeighbors(player2) should be(neighborsOfPlayer2)
        durakGame.getNeighbors(player1) should be(neighborsOfPlayer1)
        durakGame.getNeighbors(player3) should be(neighborsOfPlayer3)


        val newPlayerList: List[Player] = List(player1, player2)
        val newNeighborsOfPlayer1: (Player, Player) = (player2, player2)
        val newDurakGameWithOnlyTwoPlayers: Desk = new Desk(newPlayerList, deck, trump, currentTurn)

        newDurakGameWithOnlyTwoPlayers.getNeighbors(player1) should be(newNeighborsOfPlayer1)
      }
    }
  }
}
