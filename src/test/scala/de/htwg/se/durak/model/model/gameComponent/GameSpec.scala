package de.htwg.se.durak.model.model.gameComponent

import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.deckComponent.DeckInterface
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.Game
import de.htwg.se.durak.model.playerComponent.PlayerInterface
import de.htwg.se.durak.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.durak.model.turnComponent.TurnInterface
import de.htwg.se.durak.model.turnComponent.turnBaseImpl.Turn
import de.htwg.se.durak.util.customExceptions.LayCardFirsException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class GameSpec extends WordSpec with Matchers {
  "A Game" when {

    val player1: PlayerInterface = new Player("Martin")
    val player2: PlayerInterface = new Player("Abduhl")
    val player3: PlayerInterface = new Player("Hans")

    val players: List[PlayerInterface] = List(player1, player2, player3)

    "created" should {
      val shuffledDeck: DeckInterface = new Deck().shuffle
      val trump: CardInterface = shuffledDeck.cards.last
      val turn: TurnInterface = new Turn(player1, player2, player3)
      val active: PlayerInterface = player1

      val game: GameInterface = Game(players, shuffledDeck, trump, turn, active, Nil)

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
      val shuffledDeck: DeckInterface = new Deck().shuffle

      val game: GameInterface = new Game(players, shuffledDeck)

      "have a players list." in {
        game.players.size should be(players.size)
        game.players should be(players)
      }

      "have a deck." in {
        game.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        game.deck.cards should be(shuffledDeck.cards)
      }

      "have the last card of deck as trump card." in {
        game.trump should be(shuffledDeck.cards.last)
      }

      "have a default turn." in {
        val defaultTurn: TurnInterface = new Turn(players.head, players.head, players.head)

        game.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        game.active should be(players.head)
      }
    }

    "created with only a players list" should {

      val game: GameInterface = new Game(players)

      "have a players list." in {
        game.players.size should be(players.size)
        game.players should be(players)
      }

      "have a shuffled deck." in {
        val unshuffledDeck: DeckInterface = new Deck()

        game.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        game.deck.cards should not be unshuffledDeck
      }

      "have the last card of the shuffled deck as trump card." in {
        game.trump should be(game.deck.cards.last)
      }

      "have a default turn." in {
        val defaultTurn: TurnInterface = new Turn(players.head, players.head, players.head)

        game.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        game.active should be(players.head)
      }
    }

    "created without parameters" should {

      val game: GameInterface = new Game()

      val defaultPlayer: PlayerInterface = new Player("default")
      val defaultPlayers: List[PlayerInterface] = List(defaultPlayer)

      "have a default players list." in {
        game.players.size should be(defaultPlayers.size)
        game.players should be(defaultPlayers)
      }

      "have a shuffled deck." in {
        val unshuffledDeck: DeckInterface = new Deck()

        game.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        game.deck.cards should not be unshuffledDeck
      }

      "have the last card of the shuffled deck as trump card." in {
        game.trump should be(game.deck.cards.last)
      }

      "have a default turn." in {
        val defaultTurn: TurnInterface = new Turn(defaultPlayers.head, defaultPlayers.head, defaultPlayers.head)

        game.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        game.active should be(defaultPlayers.head)
      }
    }

    "distribute cards" should {
      val game: GameInterface = new Game(players)
      game.distributeCards(game.players)
      "give each player 5 cards" in {
        game.players.foreach(p => p.handCards.size should be(5))
      }
    }

    "started" should {

      val player1: PlayerInterface = new Player("Gerhard")
      val player2: PlayerInterface = new Player("Till")
      val player3: PlayerInterface = new Player("Karlheinz")

      val players: List[PlayerInterface] = List(player1, player2, player3)

      val game: GameInterface = new Game(players)

      val newGame: GameInterface = game.start()

      "give each player of the players list hand cards." in {
        newGame.players.foreach(player => player.handCards.size should be(5))
      }

      "return a new durak game with valid parameters." in {

        val cardsDeckTuple: (List[CardInterface], DeckInterface) = game.deck.popNCards(5 * players.size)

        newGame.players should be(players)
        newGame.deck.cards.size should be(cardsDeckTuple._2.cards.size)
      }
    }

    "a player says ok" should {
      var game: GameInterface = new Game(players)
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

      
    }

  }
}
