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
import de.htwg.se.durak.util.customExceptions.{IllegalTurnException, LayCardFirsException, MissingBlockingCardException, NoCardsToTakeException, VictimHasNotEnoughCardsToBlockException}
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
      val winners = Nil

      val game: GameInterface = Game(players, shuffledDeck, trump, turn, active, winners)

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

      "have a winners list." in {
        game.winners should be(winners)
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
      val defaultPlayer: PlayerInterface = new Player("default")
      val defaultPlayers: List[PlayerInterface] = List(defaultPlayer)

      "have a default players list." in {
        val game: GameInterface = new Game()
        game.players.size should be(defaultPlayers.size)
        game.players should be(defaultPlayers)
      }

      "have a shuffled deck." in {
        val game: GameInterface = new Game()
        val unshuffledDeck: DeckInterface = new Deck()

        game.deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        game.deck.cards should not be unshuffledDeck
      }

      "have the last card of the shuffled deck as trump card." in {
        val game: GameInterface = new Game()
        game.trump should be(game.deck.cards.last)
      }

      "have a default turn." in {
        val game: GameInterface = new Game()
        val defaultTurn: TurnInterface = new Turn(defaultPlayers.head, defaultPlayers.head, defaultPlayers.head)

        game.currentTurn should be(defaultTurn)
      }

      "have a active player." in {
        val game: GameInterface = new Game()
        game.active should be(defaultPlayers.head)
      }
    }

    "distribute cards" should {
      val game: GameInterface = new Game(players)
      val newDeck: DeckInterface = game.distributeCards(game.players)

      "should return a new deck with players.size * 5 cards less." in {
        newDeck.cards.size should be(game.deck.cards.size - players.size * 5)
      }
    }

    "started" should {
      val game: GameInterface = new Game(players)
      val startedGame: GameInterface = game.start()

      "give each player hand cards." in {
        startedGame.players.foreach(player => player.handCards.size should be(5))
      }

      "return a valid durak game." in {
        val newDeck: DeckInterface = game.distributeCards(game.players)

        startedGame.deck should be(newDeck)
      }
    }

    "a player says ok" should {
      val attackerHandCard1: CardInterface = Card(CardColor.Karo, CardValue.Zwei)
      val attackerHandCard2: CardInterface = Card(CardColor.Karo, CardValue.Drei)
      val attackerHandCard3: CardInterface = Card(CardColor.Karo, CardValue.Vier)
      val attackerHandCard4: CardInterface = Card(CardColor.Karo, CardValue.Fünf)
      val attackerHandCard5: CardInterface = Card(CardColor.Karo, CardValue.Sechs)

      val attackerHandCards: List[CardInterface] = List(attackerHandCard1, attackerHandCard2, attackerHandCard3,
        attackerHandCard4, attackerHandCard5)
      val attacker: PlayerInterface = Player("Attacker", attackerHandCards)

      val vicitmHandCard1: CardInterface = Card(CardColor.Herz, CardValue.Zwei)
      val vicitmHandCard2: CardInterface = Card(CardColor.Herz, CardValue.Drei)
      val vicitmHandCard3: CardInterface = Card(CardColor.Herz, CardValue.Vier)
      val vicitmHandCard4: CardInterface = Card(CardColor.Herz, CardValue.Fünf)
      val vicitmHandCard5: CardInterface = Card(CardColor.Herz, CardValue.Sechs)

      val victimHandCards: List[CardInterface] = List(vicitmHandCard1, vicitmHandCard2, vicitmHandCard3, vicitmHandCard4,
        vicitmHandCard5)
      val victim: PlayerInterface = Player("Victim", victimHandCards)

      val neighbourCard1: CardInterface = Card(CardColor.Pik, CardValue.Zwei)
      val neighbourCard2: CardInterface = Card(CardColor.Pik, CardValue.Drei)
      val neighbourCard3: CardInterface = Card(CardColor.Pik, CardValue.Vier)
      val neighbourCard4: CardInterface = Card(CardColor.Pik, CardValue.Fünf)
      val neighbourCard5: CardInterface = Card(CardColor.Pik, CardValue.Sechs)

      val neighbourHandCards: List[CardInterface] = List(neighbourCard1, neighbourCard2, neighbourCard3, neighbourCard4,
        neighbourCard5)
      val neighbour: PlayerInterface = Player("Neighbour", neighbourHandCards)

      val deckCard1: CardInterface = Card(CardColor.Kreuz, CardValue.Zwei)
      val deckCard2: CardInterface = Card(CardColor.Kreuz, CardValue.Drei)
      val deckCard3: CardInterface = Card(CardColor.Kreuz, CardValue.Vier)
      val deckCard4: CardInterface = Card(CardColor.Kreuz, CardValue.Fünf)
      val deckCard5: CardInterface = Card(CardColor.Kreuz, CardValue.Sechs)
      val deckCard6: CardInterface = Card(CardColor.Kreuz, CardValue.Sieben)
      val deckCard7: CardInterface = Card(CardColor.Kreuz, CardValue.Acht)
      val deckCard8: CardInterface = Card(CardColor.Kreuz, CardValue.Neun)
      val deckCard9: CardInterface = Card(CardColor.Kreuz, CardValue.Bube)
      val deckCard10: CardInterface = Card(CardColor.Herz, CardValue.Dame)

      val deckCards: List[CardInterface] = List(deckCard1, deckCard2, deckCard3, deckCard4, deckCard5, deckCard6,
        deckCard7, deckCard8, deckCard9, deckCard10)
      val deck: DeckInterface = Deck(deckCards)

      val turn: TurnInterface = Turn(attacker, victim, neighbour, Nil, Map())

      val playerList: List[PlayerInterface] = List(neighbour, victim, attacker)

      var game: GameInterface = Game(playerList, deck, deck.cards.last, turn, attacker, Nil)

      "throw an LayCardFistException if the attacker has not layed a card yet." in {
        game.active should be(attacker)
        game.currentTurn.attackCards should be(Nil)
        intercept[LayCardFirsException] {
          game.playOk()
        }
      }

      "let the victim play, if the attacker layed cards and played ok." in {
        game.active should be(attacker)
        game.currentTurn.attackCards should be(Nil)

        game = game.playCard(attackerHandCard1, None)

        game.active should be(attacker)
        game.currentTurn.attackCards should be(List(attackerHandCard1))
        game = game.playOk()

        game.active should be(victim)
      }

      "do nothing if the victim want to play ok." in {
        val oldGame: GameInterface = game
        game = game.playOk()
        oldGame should be(game)
      }


      "let the attacker play if the neighbour played ok." in {
        game = game.playCard(vicitmHandCard1, Some(attackerHandCard1))
        game = game.playOk()
        game.active should be(attacker)
      }

      "start a new turn if the attacker and neighbour played ok." in {
        game = game.playOk()

        game.active should be(victim)
        game.currentTurn.attacker should be(victim)
        game.currentTurn.victim should be(neighbour)
        game.currentTurn.neighbour should be(attacker)
        game.currentTurn.attackCards should be(Nil)
        game.currentTurn.blockedBy should be(Map())
      }
    }

    "when the victim takes the attack cards" should {
      val attackerHandCard1: CardInterface = Card(CardColor.Karo, CardValue.Zwei)
      val attackerHandCard2: CardInterface = Card(CardColor.Karo, CardValue.Drei)
      val attackerHandCard3: CardInterface = Card(CardColor.Karo, CardValue.Vier)
      val attackerHandCard4: CardInterface = Card(CardColor.Karo, CardValue.Fünf)
      val attackerHandCard5: CardInterface = Card(CardColor.Karo, CardValue.Sechs)

      val attackerHandCards: List[CardInterface] = List(attackerHandCard1, attackerHandCard2, attackerHandCard3,
        attackerHandCard4, attackerHandCard5)
      val attacker: PlayerInterface = Player("Attacker", attackerHandCards)

      val vicitmHandCard1: CardInterface = Card(CardColor.Herz, CardValue.Zwei)
      val vicitmHandCard2: CardInterface = Card(CardColor.Herz, CardValue.Drei)
      val vicitmHandCard3: CardInterface = Card(CardColor.Herz, CardValue.Vier)
      val vicitmHandCard4: CardInterface = Card(CardColor.Herz, CardValue.Fünf)
      val vicitmHandCard5: CardInterface = Card(CardColor.Herz, CardValue.Sechs)

      val victimHandCards: List[CardInterface] = List(vicitmHandCard1, vicitmHandCard2, vicitmHandCard3, vicitmHandCard4,
        vicitmHandCard5)
      val victim: PlayerInterface = Player("Victim", victimHandCards)

      val neighbourCard1: CardInterface = Card(CardColor.Pik, CardValue.Zwei)
      val neighbourCard2: CardInterface = Card(CardColor.Pik, CardValue.Drei)
      val neighbourCard3: CardInterface = Card(CardColor.Pik, CardValue.Vier)
      val neighbourCard4: CardInterface = Card(CardColor.Pik, CardValue.Fünf)
      val neighbourCard5: CardInterface = Card(CardColor.Pik, CardValue.Sechs)

      val neighbourHandCards: List[CardInterface] = List(neighbourCard1, neighbourCard2, neighbourCard3, neighbourCard4,
        neighbourCard5)
      val neighbour: PlayerInterface = Player("Neighbour", neighbourHandCards)

      val deckCard1: CardInterface = Card(CardColor.Kreuz, CardValue.Zwei)
      val deckCard2: CardInterface = Card(CardColor.Kreuz, CardValue.Drei)
      val deckCard3: CardInterface = Card(CardColor.Kreuz, CardValue.Vier)
      val deckCard4: CardInterface = Card(CardColor.Kreuz, CardValue.Fünf)
      val deckCard5: CardInterface = Card(CardColor.Kreuz, CardValue.Sechs)
      val deckCard6: CardInterface = Card(CardColor.Kreuz, CardValue.Sieben)
      val deckCard7: CardInterface = Card(CardColor.Kreuz, CardValue.Acht)
      val deckCard8: CardInterface = Card(CardColor.Kreuz, CardValue.Neun)
      val deckCard9: CardInterface = Card(CardColor.Kreuz, CardValue.Bube)
      val deckCard10: CardInterface = Card(CardColor.Herz, CardValue.Dame)

      val deckCards: List[CardInterface] = List(deckCard1, deckCard2, deckCard3, deckCard4, deckCard5, deckCard6,
        deckCard7, deckCard8, deckCard9, deckCard10)
      val deck: DeckInterface = Deck(deckCards)

      val turn: TurnInterface = Turn(attacker, victim, neighbour, Nil, Map())

      val playerList: List[PlayerInterface] = List(neighbour, victim, attacker)

      var game: GameInterface = Game(playerList, deck, deck.cards.last, turn, attacker, Nil)

      "throw a NoCardsToTakeException if the current player is not the victim" in {
        intercept[NoCardsToTakeException] {
          game.takeCards()
        }
      }

      "start a new turn if the victim is the active player." in {
        game = game.playCard(attackerHandCard1, None)
        game = game.playOk()
        game = game.takeCards()

        val nextTurn: TurnInterface = Turn(neighbour, attacker, victim, Nil, Map())
        game.currentTurn should be(nextTurn)
      }
    }

    "a player is playing a card" should {

      val card1 = Card(CardColor.Herz, CardValue.Acht)

      val card2 = Card(CardColor.Herz, CardValue.Zwei)

      val player1 = Player("1", card1::Nil)

      val player2 = Player("2", card2::Nil)

      val player3 = Player("4", Nil)

      val players = List(player1, player2, player3)

      val turn = Turn(player1, player2, player3, List(Card(CardColor.Herz, CardValue.Sieben), Card(CardColor.Herz, CardValue.Sechs)), Map())

      val deck = new Deck()

      val game = Game(players, deck, deck.head, turn, turn.attacker, Nil)

      "throw an VictimHasNotEnoughCardsToBlockException if the victim has not enough cards to block" in {
        intercept[VictimHasNotEnoughCardsToBlockException] {
          game.playCard(card1, None)
        }
      }

      "result in the same game if he hasnt got the card" in {
        game.playCard(card2, None).equals(game)
      }
    }

    "a player is shoving a card" should {
      "may not be able to shove" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)

        val player1 = Player("1", Nil)
        val player2 = Player("2", card::Nil)
        val player3 = Player("4", Nil)

        val players = List(player1, player2, player3)
        val turn = Turn(player1, player2, player3, List(Card(CardColor.Herz, CardValue.Sieben)), Map())
        val deck = new Deck()

        var game = Game(players, deck, deck.head, turn, turn.victim, Nil)

        intercept[IllegalTurnException] {
          game.playCard(card, None)
        }
      }

      "may be able to shove" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Karo, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)

        val player1 = Player("1", Nil)
        val player2 = Player("2", card::card2::Nil)
        val player3 = Player("4", card3::card4::Nil)

        val players = List(player1, player2, player3)
        val turn = Turn(player1, player2, player3, List(Card(CardColor.Herz, CardValue.Zwei)), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.victim, Nil)

        val newGame = game.playCard(card, None)
        newGame.active.equals(player2)
      }

      "may be able to win by doing so" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Karo, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)

        val player1 = Player("1", card2::Nil)
        val player2 = Player("2", card::Nil)
        val player3 = Player("4", card3::card4::Nil)

        val players = List(player1, player2, player3)
        val turn = Turn(player1, player2, player3, List(Card(CardColor.Herz, CardValue.Zwei)), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.victim, Nil)

        val newGame = game.playCard(card, None)

        newGame.winners.contains(player2)

      }
      "may be able to win by doing so with may others" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Karo, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", card2::Nil)
        val player3 = Player("4", card3::card4::Nil)
        val player4 = Player("4", card3::card4::Nil)
        val player5 = Player("4", card3::card4::Nil)

        val players = List(player1, player2, player3, player4, player5)
        val turn = Turn(player1, player2, player3, List(Card(CardColor.Herz, CardValue.Zwei)), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.victim, Nil)

        val newGame = game.playCard(card, None)

        newGame.winners.contains(player2)
      }

    }

    "a player is defending" should {
      "may be able to win by doing so with many others" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Herz, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)
        val card5 = Card(CardColor.Herz, CardValue.Zwei)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", card2::Nil)
        val player3 = Player("4", card3::card4::Nil)
        val player4 = Player("4", card3::card4::Nil)
        val player5 = Player("4", card3::card4::Nil)

        val players = List(player1, player2, player3, player4, player5)
        val turn = Turn(player1, player2, player3, List(card5), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.victim, Nil)

        val newGame = game.playCard(card2, Some(card5))

        newGame.winners.contains(player2)
      }

      "may be able to win by doing so with two others" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Herz, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)
        val card5 = Card(CardColor.Herz, CardValue.Zwei)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", card2::Nil)
        val player3 = Player("4", card3::card4::Nil)

        val players = List(player1, player2, player3)
        val turn = Turn(player1, player2, player3, List(card5), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.victim, Nil)

        val newGame = game.playCard(card2, Some(card5))

        newGame.winners.contains(player2)
      }

      "may be able to win by doing so with one other player" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Herz, CardValue.Drei)
         val card5 = Card(CardColor.Herz, CardValue.Zwei)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", card2::Nil)

        val players = List(player1, player2)
        val turn = Turn(player1, player2, player1, List(card5), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.victim, Nil)

        val newGame = game.playCard(card2, Some(card5))

        newGame.winners.contains(player2)
      }

      "may just block the attack card" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Herz, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)
        val card5 = Card(CardColor.Herz, CardValue.Zwei)
        val card6 = Card(CardColor.Herz, CardValue.Ass)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", card2::card6::Nil)
        val player3 = Player("4", card3::card4::Nil)

        val players = List(player1, player2, player3)
        val turn = Turn(player1, player2, player3, List(card5), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.victim, Nil)

        val newGame = game.playCard(card2, Some(card5))

        newGame.currentTurn.blockedBy.contains(card2)
      }

      "throw an exception if there is no card to block specified" in {
        val card = Card(CardColor.Kreuz, CardValue.Fünf)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", Nil)
        val player3 = Player("4", Nil)

        val players = List(player1, player2, player3)
        val turn = Turn(player1, player2, player3, List(), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.victim, Nil)

        intercept[MissingBlockingCardException] {
          game.defend(card, None)
        }
      }
    }

    "a player is attacking" should {
      "be able to win with many other players" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Herz, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)
        val card5 = Card(CardColor.Herz, CardValue.Zwei)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", card2::Nil)
        val player3 = Player("4", card3::card4::Nil)
        val player4 = Player("4", card3::card4::Nil)
        val player5 = Player("4", card3::card4::Nil)

        val players = List(player1, player2, player3)
        val turn = Turn(player1, player2, player3, List(), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.attacker, Nil)

        val newGame = game.attack(card)

        newGame.winners.contains(player1)
      }

      "be able to win with two other players" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Herz, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", card2::Nil)
        val player3 = Player("4", card3::card4::Nil)

        val players = List(player1, player2, player3)
        val turn = Turn(player1, player2, player3, List(), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.attacker, Nil)

        val newGame = game.attack(card)

        newGame.winners.contains(player1)
      }

      "be able to win with one other player" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Herz, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", card2::Nil)

        val players = List(player1, player2, player1)
        val turn = Turn(player1, player2, player1, List(), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.attacker, Nil)

        val newGame = game.attack(card)

        newGame.winners.contains(player1)
      }

      "be able to raise an exception" in {
        val card = Card(CardColor.Karo, CardValue.Zwei)
        val card2 = Card(CardColor.Herz, CardValue.Drei)
        val card3 = Card(CardColor.Kreuz, CardValue.Vier)
        val card4 = Card(CardColor.Pik, CardValue.Fünf)

        val player1 = Player("1", card::Nil)
        val player2 = Player("2", card2::Nil)

        val players = List(player1, player2, player1)
        val turn = Turn(player1, player2, player1, List(card3), Map())
        val deck = new Deck()

        val game = Game(players, deck, deck.head, turn, turn.attacker, Nil)

        intercept[IllegalTurnException] {
          game.attack(card)
        }
      }
    }
  }
}
