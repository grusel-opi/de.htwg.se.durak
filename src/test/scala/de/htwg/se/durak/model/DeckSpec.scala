package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DeckSpec extends WordSpec with Matchers {
  "A Deck" when {

    "created with predefined cards" should {
      val card1: Card = Card(CardColor.Herz, CardValue.Zwei)
      val card2: Card = Card(CardColor.Herz, CardValue.Drei)
      val card3: Card = Card(CardColor.Herz, CardValue.Vier)
      val card4: Card = Card(CardColor.Herz, CardValue.Sechs)
      val card5: Card = Card(CardColor.Herz, CardValue.Sieben)

      val cards: List[Card] = List(card1, card2, card3, card4, card5)
      val deck: Deck = Deck(cards)

      "contain predefined cards." in {
        deck.cards.size should be(cards.size)
        deck.cards should be(cards)
      }
    }

    "created without predefined cards" should {
      val cardsShouldBeInDeck: List[Card] = for {c <- CardColor.values.toList; v <- CardValue.values.toList} yield Card(c, v)
      val deck: Deck = new Deck()

      "have 52 cards" in {
        deck.cards.size should be(CardColor.values.toList.size * CardValue.values.toList.size)
        cardsShouldBeInDeck.foreach(c => deck.cards.contains(c))
      }

      "have a nice string representation." in {
        val stringRepresentation: String = "Deck: " + deck.cards.mkString(", ")
        deck.toString should be(stringRepresentation)
      }
    }

    "shuffled" should {
      "have a random card ordering." in {
        val normalDeck: Deck = new Deck()
        val shuffledDeck: Deck = new Deck().shuffle

        shuffledDeck.cards.size should be(52)
        shuffledDeck.cards should not be(normalDeck)
      }
    }

    "getting the head card" should {
      "return the top card of the deck." in {
        val card1: Card = Card(CardColor.Kreuz, CardValue.Zehn)
        val card2: Card = Card(CardColor.Herz, CardValue.Ass)
        val cardsInDeck: List[Card] = List(card1, card2)

        val deck: Deck = Deck(cardsInDeck)

        deck.cards.size should be (cardsInDeck.size)
        deck.cards should be(cardsInDeck)
        deck.head should be(card1)
      }
    }

    "getting the tail" should {
      val deck: Deck = new Deck()
      "return the cards of the deck, except the top card." in {
        val cardsShouldBeInDeck: List[Card] = for {c <- CardColor.values.toList; v <- CardValue.values.toList} yield Card(c, v)

        deck.cards.size should be(cardsShouldBeInDeck.size)
        deck.cards should be(cardsShouldBeInDeck)

        val newDeck: Deck = deck.tail

        newDeck.cards.size should be(cardsShouldBeInDeck.size -1)
      }
    }

    "pop the top card" should {
      val card1: Card = Card(CardColor.Herz, CardValue.Zwei)
      val card2: Card = Card(CardColor.Karo, CardValue.Ass)
      val card3: Card = Card(CardColor.Kreuz, CardValue.Fünf)

      val cardsInDeck: List[Card] = List(card1, card2, card3)
      val deck: Deck = Deck(cardsInDeck)

      "have one card less as before." in {
        deck.cards.size should be(cardsInDeck.size)
        deck.cards should be(cardsInDeck)

        val cardDeckTuple: (Card, Deck) = deck.popTopCard()
        val returnedCard: Card = cardDeckTuple._1
        val newDeck: Deck = cardDeckTuple._2

        returnedCard should be(card1)

        newDeck.cards.size should be(deck.cards.size - 1)
        newDeck.cards should be(List(card2, card3))
      }
    }

    "pop two cards" should {
      val card1: Card = Card(CardColor.Herz, CardValue.Zwei)
      val card2: Card = Card(CardColor.Karo, CardValue.Ass)
      val card3: Card = Card(CardColor.Kreuz, CardValue.Fünf)

      val cardsInDeck: List[Card] = List(card1, card2, card3)
      val deck: Deck = Deck(cardsInDeck)

      "have two cards less as before" in {
        deck.cards.size should be(cardsInDeck.size)
        deck.cards should be(cardsInDeck)

        val cardsDeckTuple: (List[Card], Deck) = deck.popNCards(2)
        val returnedCards: List[Card] = cardsDeckTuple._1
        val newDeck: Deck = cardsDeckTuple._2

        returnedCards.size should be (2)
        returnedCards should be (List(card1, card2))

        newDeck.cards.size should be(deck.cards.size - 2)
        newDeck.cards should be(List(card3))
      }
    }
  }
}
