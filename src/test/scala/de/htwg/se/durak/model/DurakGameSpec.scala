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

    "created" should {
      val shuffledDeck: Deck = new Deck().shuffle
      val trump: Card = shuffledDeck.cards.last
      val turn: Turn = new Turn(player1, player2, player3)
      val active: Player = player1
      val satisfiedPlayers: List[Player] = Nil

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

    "created with only a players list and a deck" should {
      val shuffledDeck: Deck = new Deck().shuffle

      val durakGame: DurakGame = new DurakGame(players, shuffledDeck)

      "have a players list." in {
        durakGame.players.size should be(players.size)
        durakGame.players should be(players)
      }

      "have a deck." in {
        durakGame.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        durakGame.deck.cards should be(shuffledDeck.cards)
      }

      "have the last card of deck as trump card." in {
        durakGame.trump should be(shuffledDeck.cards.last)
      }

      "have a default turn." in {
        val defaultTurn: Turn = new Turn(players.head, players.head, players.head)

        durakGame.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        durakGame.active should be(players.head)
      }

      "have a empty list of satisfied players." in {
        durakGame.ok.size should be(0)
        durakGame.ok should be(Nil)
      }
    }

    "created with only a players list" should {

      val durakGame: DurakGame = new DurakGame(players)

      "have a players list." in {
        durakGame.players.size should be(players.size)
        durakGame.players should be(players)
      }

      "have a shuffled deck." in {
        val unshuffledDeck: Deck = new Deck()

        durakGame.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        durakGame.deck.cards should not be (unshuffledDeck)
      }

      "have the last card of the shuffled deck as trump card." in {
        durakGame.trump should be(durakGame.deck.cards.last)
      }

      "have a default turn." in {
        val defaultTurn: Turn = new Turn(players.head, players.head, players.head)

        durakGame.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        durakGame.active should be(players.head)
      }

      "have a empty list of satisfied players." in {
        durakGame.ok.size should be(0)
        durakGame.ok should be(Nil)
      }
    }

    "created without paremeters" should {

      val durakGame: DurakGame = new DurakGame()

      val defaultPlayer: Player = new Player("default")
      val defaultPlayers: List[Player] = List(defaultPlayer)

      "have a default players list." in {
        durakGame.players.size should be(defaultPlayers.size)
        durakGame.players should be(defaultPlayers)
      }

      "have a shuffled deck." in {
        val unshuffledDeck: Deck = new Deck()

        durakGame.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        durakGame.deck.cards should not be (unshuffledDeck)
      }

      "have the last card of the shuffled deck as trump card." in {
        durakGame.trump should be(durakGame.deck.cards.last)
      }

      "have a default turn." in {
        val defaultTurn: Turn = new Turn(defaultPlayers.head, defaultPlayers.head, defaultPlayers.head)

        durakGame.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        durakGame.active should be(defaultPlayers.head)
      }

      "have a empty list of satisfied players." in {
        durakGame.ok.size should be(0)
        durakGame.ok should be(Nil)
      }
    }

    "started" should {

      val player1: Player = new Player("Gerhard")
      val player2: Player = new Player("Till")
      val player3: Player = new Player("Karlheinz")

      val players: List[Player] = List(player1, player2, player3)

      val durakGame: DurakGame = new DurakGame(players)

      val newDurakGame: DurakGame = durakGame.start

      "give each player of the players list hand cards." in {
        newDurakGame.players.foreach(player => player.handCards.size should be(5))
      }

      "return a new durak game with valid parameters." in {

        val cardsDeckTuple: (List[Card], Deck) = durakGame.deck.popNCards(5 * players.size)
        val newDeck: Deck = cardsDeckTuple._2

        newDurakGame.players should be(players)
        newDurakGame.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size
          - players.size * 5)
        newDurakGame.trump should be(durakGame.trump)
        newDurakGame.ok should be(Nil)
      }
    }

    "add a player" should {

      val durakGame: DurakGame = new DurakGame(players)

      "have one player more in the players list." in {
        val playerToAdd: Player = new Player("Schmuserkatzer")
        val newPlayers: List[Player] = playerToAdd :: players
        val newDurakGame: DurakGame = durakGame.addPlayer(playerToAdd)

        durakGame.players.size should be(players.size)
        durakGame.players should be(players)

        newDurakGame.players.size should be(players.size + 1)
        newDurakGame.players should be(newPlayers)
      }
    }

    "a player wins" should {

      val shufledDeck: Deck = new Deck().shuffle
      val turn: Turn = new Turn(player1, player2, player3)

      val durakGame: DurakGame = DurakGame(players, shufledDeck, shufledDeck.cards.last, turn, player1, Nil)

      "remove the player from the players list." in {
        val newDurakGame: DurakGame = durakGame.win

        newDurakGame.players.size should be(players.size - 1)
        newDurakGame.players should be(players.filterNot(p => p.equals(player1)))
      }
    }

    "a player is satified with his turn" should {

      val shuffledDeck: Deck = new Deck().shuffle

      val attackCard1: Card = Card(CardColor.Herz, CardValue.Neun)
      val attackCard2: Card = Card(CardColor.Kreuz, CardValue.Neun)
      val attackCards: List[Card] = List(attackCard1, attackCard2)

      val turn: Turn = Turn(player1, player2, player3, attackCards, Map())

      val durakGame: DurakGame = new DurakGame(players, shuffledDeck, shuffledDeck.cards.last, turn, player1, Nil)

      "add the player to the satisfied players list." in {
//        val newDurakGame: DurakGame = durakGame.playOk
//
//        newDurakGame.active should be(player2)
//        newDurakGame.ok.size should be(1)
//        newDurakGame.ok should be(player1)
      }

      "should set the next player active." in {

      }
    }
  }
}
