package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DeckSpec extends WordSpec with Matchers {
  "A Deck" when {

    "created with predefined cards" should {
      val card1: Card = Card(CardColor.Herz, CardValue.Two)
      val card2: Card = Card(CardColor.Herz, CardValue.Three)
      val card3: Card = Card(CardColor.Herz, CardValue.Four)
      val card4: Card = Card(CardColor.Herz, CardValue.Six)
      val card5: Card = Card(CardColor.Herz, CardValue.Seven)

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
        val card1: Card = Card(CardColor.Kreuz, CardValue.Ten)
        val card2: Card = Card(CardColor.Herz, CardValue.Ace)
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
  }
}
