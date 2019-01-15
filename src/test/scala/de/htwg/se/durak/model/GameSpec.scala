package de.htwg.se.durak.model

import de.htwg.se.durak.model.cardComponent.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.{Game, Turn}
import de.htwg.se.durak.model.playerComponent.Player
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameSpec extends WordSpec with Matchers {
   "A Game" when {

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

      val game: Game = Game(players, shuffledDeck, trump, turn, active, Nil)

      "have a players list." in {
        game.players.size should be(players.size)
        game.players should be(players)
      }

      "have a deck." in {
        game.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        game.deck.cards should be(shuffledDeck.cards)
      }

      "have a trump card." in {
        game.trump should be(trump)
      }

      "have a turn." in {
        game.currentTurn should be(turn)
      }

      "have a active player." in {
        game.active should be(player1)
      }
    }

    "created with only a players list and a deck" should {
      val shuffledDeck: Deck = new Deck().shuffle

      val Game: Game = new Game(players, shuffledDeck)

      "have a players list." in {
        Game.players.size should be(players.size)
        Game.players should be(players)
      }

      "have a deck." in {
        Game.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        Game.deck.cards should be(shuffledDeck.cards)
      }

      "have the last card of deck as trump card." in {
        Game.trump should be(shuffledDeck.cards.last)
      }

      "have a default turn." in {
        val defaultTurn: Turn = new Turn(players.head, players.head, players.head)

        Game.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        Game.active should be(players.head)
      }
    }

    "created with only a players list" should {

      val Game: Game = new Game(players)

      "have a players list." in {
        Game.players.size should be(players.size)
        Game.players should be(players)
      }

      "have a shuffled deck." in {
        val unshuffledDeck: Deck = new Deck()

        Game.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        Game.deck.cards should not be unshuffledDeck
      }

      "have the last card of the shuffled deck as trump card." in {
        Game.trump should be(Game.deck.cards.last)
      }

      "have a default turn." in {
        val defaultTurn: Turn = new Turn(players.head, players.head, players.head)

        Game.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        Game.active should be(players.head)
      }
    }

    "created without parameters" should {

      val Game: Game = new Game()

      val defaultPlayer: Player = new Player("default")
      val defaultPlayers: List[Player] = List(defaultPlayer)

      "have a default players list." in {
        Game.players.size should be(defaultPlayers.size)
        Game.players should be(defaultPlayers)
      }

      "have a shuffled deck." in {
        val unshuffledDeck: Deck = new Deck()

        Game.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        Game.deck.cards should not be unshuffledDeck
      }

      "have the last card of the shuffled deck as trump card." in {
        Game.trump should be(Game.deck.cards.last)
      }

      "have a default turn." in {
        val defaultTurn: Turn = new Turn(defaultPlayers.head, defaultPlayers.head, defaultPlayers.head)

        Game.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        Game.active should be(defaultPlayers.head)
      }
    }

    "started" should {

      val player1: Player = new Player("Gerhard")
      val player2: Player = new Player("Till")
      val player3: Player = new Player("Karlheinz")

      val players: List[Player] = List(player1, player2, player3)

      val Game: Game = new Game(players)

      val newGame: Game = Game.start()

      "give each player of the players list hand cards." in {
        newGame.players.foreach(player => player.handCards.size should be(5))
      }

      "return a new durak game with valid parameters." in {

        val cardsDeckTuple: (List[Card], Deck) = Game.deck.popNCards(5 * players.size)
        val newDeck: Deck = cardsDeckTuple._2

        newGame.players should be(players)
        newGame.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size
          - players.size * 5)
        newGame.trump should be(Game.trump)
      }
    }

    "a player is satisfied with his turn" should {

      val shuffledDeck: Deck = new Deck().shuffle

      val attackCard1: Card = Card(CardColor.Herz, CardValue.Neun)
      val attackCard2: Card = Card(CardColor.Kreuz, CardValue.Neun)
      val attackCards: List[Card] = List(attackCard1, attackCard2)

      val turn: Turn = Turn(player1, player2, player3, attackCards, Map())

      val game: Game = Game(players, shuffledDeck, shuffledDeck.cards.last, turn, player1, Nil)

      "should set the next player active." in {

      }
    }
  }
}
