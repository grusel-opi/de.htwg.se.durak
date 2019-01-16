package de.htwg.se.durak.model.model.gameComponent

import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.{Game, Turn}
import de.htwg.se.durak.model.playerComponent.Player
import de.htwg.se.durak.util.customExceptions.LayCardFirsException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

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

    "distribute cards" should {
      val game = new Game(players)
      game.distributeCards(game.players)
      "give each player 5 cards" in {
        game.players.foreach(p => p.handCards.size should be(5))
      }
    }

    "started" should {

      val player1: Player = new Player("Gerhard")
      val player2: Player = new Player("Till")
      val player3: Player = new Player("Karlheinz")

      val players: List[Player] = List(player1, player2, player3)

      val game: Game = new Game(players)

      val newGame: Game = game.start()

      "give each player of the players list hand cards." in {
        newGame.players.foreach(player => player.handCards.size should be(5))
      }

      "return a new durak game with valid parameters." in {

        val cardsDeckTuple: (List[Card], Deck) = game.deck.popNCards(5 * players.size)

        newGame.players should be(players)
        newGame.deck.cards.size should be(cardsDeckTuple._2.cards.size)
      }
    }

    "a player says ok" should {
      var game = new Game(players)
      game = game.start()
      "throw an LayCardFistException if he has not layed a card yet" in {
        intercept[LayCardFirsException] {
          game.playOk()
        }
      }
      "let the next player play" in {
        game = game.playCard(game.active.handCards.head, None)
        game = game.playOk()
        game.active should be(game.getNeighbour(game.currentTurn.attacker))
      }
      "let the turn close if victim has blocked all cards" in {
        val card0 = Card(CardColor.Herz, CardValue.Acht)
        val card1 = Card(CardColor.Herz, CardValue.Neun)
        val turn = Turn(player1, player2, player3, Nil, Map(card0 -> card1))
        var tmpGame = Game(players, new Deck(), card0, turn, player1, Nil)
        tmpGame = tmpGame.playOk()
        tmpGame.active should be theSameInstanceAs player2
      }
    }

  }
}
