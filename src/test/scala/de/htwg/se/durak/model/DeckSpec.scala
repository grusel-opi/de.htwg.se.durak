package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DeckSpec extends WordSpec with Matchers {
  val deck = new Deck()

  "A deck" when {
    "created without definined cards" should {
      "have 52 cards" in {
        deck.cards.size should be(52)
      }

      "have a nice string representation." in {
        val cardsInDeck: List[Card] = for {c <- CardColor.values.toList; v <- CardValue.values} yield Card(c, v)

        deck.toString should be("Deck: " + cardsInDeck.mkString(", "))
      }
    }

    "picking one top card" should {
      "have one card less than before." in {
        val cardDeckTuple: (Card, Deck) = deck.popTopCard()
        val pickedCard: Card = cardDeckTuple._1
        val newDeck: Deck = cardDeckTuple._2

        newDeck.cards.size should be (deck.cards.size - 1)
      }
    }

    "picking five top cards" should {
      "have five cards less than before." in {
        val cardsDeckTuple: (List[Card], Deck) = deck.popNCards(5)
        val pickedCards: List[Card] = cardsDeckTuple._1
        val newDeck: Deck = cardsDeckTuple._2

        newDeck.cards.size should be(deck.cards.size - 5)
      }
    }

    "getting last card" should {
      "return the last card of the deck" in {
        deck.lastCard should be(deck.cards(deck.cards.size - 1))
      }
    }

    "shuffled" should {
      "return a shuffled deck." in {
        val shuffledDeck: Deck = deck.shuffle

        shuffledDeck.cards should not be(deck)
      }
    }
  }
}
