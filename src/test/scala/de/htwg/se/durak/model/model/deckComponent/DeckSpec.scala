package de.htwg.se.durak.model.model.deckComponent

import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.deckComponent.DeckInterface
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import play.api.libs.json.JsObject

import scala.xml.{Node, Utility}

@RunWith(classOf[JUnitRunner])
class DeckSpec extends WordSpec with Matchers {
  "A Deck" when {

    "created with predefined cards" should {
      val card1: CardInterface = Card(CardColor.Herz, CardValue.Zwei)
      val card2: CardInterface = Card(CardColor.Herz, CardValue.Drei)
      val card3: CardInterface = Card(CardColor.Herz, CardValue.Vier)
      val card4: CardInterface = Card(CardColor.Herz, CardValue.Sechs)
      val card5: CardInterface = Card(CardColor.Herz, CardValue.Sieben)

      val cards: List[CardInterface] = List(card1, card2, card3, card4, card5)
      val deck: DeckInterface = Deck(cards)

      "contain predefined cards." in {
        deck.cards.size should be(cards.size)
        deck.cards should be(cards)
      }
    }

    "created without predefined cards" should {
      val cardsShouldBeInDeck: List[CardInterface] = for {c <- CardColor.values.toList; v <- CardValue.values.toList} yield Card(c, v)
      val deck: DeckInterface = new Deck()

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
        val normalDeck: DeckInterface = new Deck()
        val shuffledDeck: DeckInterface = new Deck().shuffle

        shuffledDeck.cards.size should be(52)
        shuffledDeck.cards should not be(normalDeck)
      }
    }

    "getting the head card" should {
      "return the top card of the deck." in {
        val card1: CardInterface = Card(CardColor.Kreuz, CardValue.Zehn)
        val card2: CardInterface = Card(CardColor.Herz, CardValue.Ass)
        val cardsInDeck: List[CardInterface] = List(card1, card2)

        val deck: DeckInterface = Deck(cardsInDeck)

        deck.cards.size should be (cardsInDeck.size)
        deck.cards should be(cardsInDeck)
        deck.head should be(card1)
      }
    }

    "getting the tail" should {
      val deck: DeckInterface = new Deck()
      "return the cards of the deck, except the top card." in {
        val cardsShouldBeInDeck: List[CardInterface] = for {c <- CardColor.values.toList; v <- CardValue.values.toList} yield Card(c, v)

        deck.cards.size should be(cardsShouldBeInDeck.size)
        deck.cards should be(cardsShouldBeInDeck)

        val newDeck: DeckInterface = deck.tail

        newDeck.cards.size should be(cardsShouldBeInDeck.size -1)
      }
    }

    "pop the top card" should {
      val card1: CardInterface = Card(CardColor.Herz, CardValue.Zwei)
      val card2: CardInterface = Card(CardColor.Karo, CardValue.Ass)
      val card3: CardInterface = Card(CardColor.Kreuz, CardValue.Fünf)

      val cardsInDeck: List[CardInterface] = List(card1, card2, card3)
      val deck: DeckInterface = Deck(cardsInDeck)

      "have one card less as before." in {
        deck.cards.size should be(cardsInDeck.size)
        deck.cards should be(cardsInDeck)

        val cardDeckTuple: (CardInterface, DeckInterface) = deck.popTopCard()
        val returnedCard: CardInterface = cardDeckTuple._1
        val newDeck: DeckInterface = cardDeckTuple._2

        returnedCard should be(card1)

        newDeck.cards.size should be(deck.cards.size - 1)
        newDeck.cards should be(List(card2, card3))
      }
    }

    "pop two cards" should {
      val card1: CardInterface = Card(CardColor.Herz, CardValue.Zwei)
      val card2: CardInterface = Card(CardColor.Karo, CardValue.Ass)
      val card3: CardInterface = Card(CardColor.Kreuz, CardValue.Fünf)

      val cardsInDeck: List[CardInterface] = List(card1, card2, card3)
      val deck: DeckInterface = Deck(cardsInDeck)

      "have two cards less as before" in {
        deck.cards.size should be(cardsInDeck.size)
        deck.cards should be(cardsInDeck)

        val cardsDeckTuple: (List[CardInterface], DeckInterface) = deck.popNCards(2)
        val returnedCards: List[CardInterface] = cardsDeckTuple._1
        val newDeck: DeckInterface = cardsDeckTuple._2

        returnedCards.size should be (2)
        returnedCards should be (List(card1, card2))

        newDeck.cards.size should be(deck.cards.size - 2)
        newDeck.cards should be(List(card3))
      }
    }

    "pop more cards as available in deck" should {
      val card1: CardInterface = Card(CardColor.Herz, CardValue.Zwei)
      val card2: CardInterface = Card(CardColor.Karo, CardValue.Ass)
      val card3: CardInterface = Card(CardColor.Kreuz, CardValue.Fünf)

      val cardsInDeck: List[CardInterface] = List(card1, card2, card3)
      val deck: DeckInterface = Deck(cardsInDeck)

      "return the left cards in deck." in {
        deck.cards.size should be(cardsInDeck.size)
        deck.cards should be(cardsInDeck)

        val cardsDeckTuple: (List[CardInterface], DeckInterface) = deck.popNCards(5)
        val returnedCards: List[CardInterface] = cardsDeckTuple._1
        val newDeck: DeckInterface = cardsDeckTuple._2

        returnedCards.size should be(deck.cards.size)
        returnedCards should be(cardsInDeck)

        newDeck.cards.size should be(0)
        newDeck.cards should be(Nil)
      }
    }

    "parsed to XML" should {
      "return the deck as a XML structure." in {
        val deck = new Deck()
        val deckAsXml: Node = Utility.trim(deck.toXml)
        val expectedXmlDeckStructure: Node =
          Utility.trim(
            <deck>
            {deck.cards.map(c => c.toXml)}
            </deck>
          )

        deckAsXml should be(expectedXmlDeckStructure)
      }
    }

    "parsed to JSON" should {
      "return the deck as a JSON structure." in {
        val deck = new Deck()
        val deckAsJson: List[JsObject] = deck.toJson
        val expectedJsonDeckStructure: List[JsObject] =
          deck.cards.map(c => c.toJson)

        deckAsJson should be(expectedJsonDeckStructure)
      }
    }
  }
}
