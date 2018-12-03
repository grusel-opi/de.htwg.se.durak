package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DurakGameSpec extends WordSpec with Matchers {
  "A DurakGame" when {

    val player1: Player = new Player("Martin")
    val player2: Player = new Player("Abduhl")
    val player3: Player = new Player("Hans")

    val players: List[Player] = List(player1, player2, player3)

    val shuffledDeck: Deck = new Deck().shuffle
    val trump: Card = shuffledDeck.cards.last

    val turn: Turn = new Turn(player1, player2, player3)

    val active: Player = player1

    val satisfiedPlayers: List[Player] = Nil

    "created" should {

      val durakGame: DurakGame = DurakGame(players, shuffledDeck, trump, turn, active, satisfiedPlayers)

      "have a players list." in {
        durakGame.players.size should be(players.size)
        durakGame.players should be(players)
      }

      "have a deck." in {
        durakGame.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        durakGame.deck.cards should be(shuffledDeck.cards)
      }

      "have a trump card." in {
        durakGame.trump should be(trump)
      }

      "have a turn." in {
        durakGame.currentTurn should be(turn)
      }

      "have a active player." in {
        durakGame.active should be(player1)
      }

      "have a list of satisfied players." in {
        durakGame.ok.size should be(0)
        durakGame.ok should be(Nil)
      }
    }
  }
}
