package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec with Matchers {
  val handCards: List[Card] = List(Card(CardColor.Diamond, CardValue.Two), Card(CardColor.Diamond, CardValue.Three),
    Card(CardColor.Diamond, CardValue.Four), Card(CardColor.Diamond, CardValue.Five))
  val playerWithFourHandCards: Player = Player("Hans", handCards)
  "A Player" when {
    "created with hand cards" should {
      "have a name." in {
        playerWithFourHandCards.name should be("Hans")
      }

      "have a nice string representation." in {
        playerWithFourHandCards.toString should be("Hans")
      }

      "have hand cards." in {
        playerWithFourHandCards.handCards.size should be(handCards.size)
        playerWithFourHandCards.handCards.equals(handCards)
      }
    }

    val playerWithoutHandCards: Player = new Player("Peter")

    "created without hand cards" should {
      "have a name." in {
        playerWithoutHandCards.name should be("Peter")
      }

      "have a nice string representation." in {
        playerWithoutHandCards.toString should be("Peter")
      }

      "have empty hand cards." in {
        playerWithoutHandCards.handCards.size should be(0)
      }
    }

    "picking one card" should {
      "have one hand card." in {
        val cardToPick: Card = Card(CardColor.Hearts, CardValue.Ace)
        val playerWithOneHandCard: Player = playerWithoutHandCards.pickCard(cardToPick)

        playerWithOneHandCard.handCards.size should be(1)
      }
    }

    "picking four cards" should {
      "have four hand cards." in {
        val cardsToPick: List[Card] = List(Card(CardColor.Diamond, CardValue.Ace), Card(CardColor.Hearts, CardValue.Ace),
          Card(CardColor.Clubs, CardValue.Ace), Card(CardColor.Spades, CardValue.Ace))
        val playerWithFourHandCards: Player = playerWithoutHandCards.pickCards(cardsToPick)

        playerWithFourHandCards.handCards.size should be(cardsToPick.size)
      }
    }

    "playing one card" should {
      "have one card less than before." in {
        val cardToPlay: Card = Card(CardColor.Diamond, CardValue.Four)
        val playerWithThreeHandCards: Player = playerWithFourHandCards.removeCard(cardToPlay)

        playerWithThreeHandCards.handCards.size should be(playerWithFourHandCards.handCards.size - 1)
      }
    }

    "playing three cards" should {
      "have three cards less than before." in {
        val cardsToPlay: List[Card] = List(Card(CardColor.Diamond, CardValue.Two), Card(CardColor.Diamond, CardValue.Three),
          Card(CardColor.Diamond, CardValue.Four))
        val playerWithOneHandCard = playerWithFourHandCards.removeCards(cardsToPlay)

        playerWithOneHandCard.handCards.size should be(playerWithFourHandCards.handCards.size - 3)
      }
    }
  }
}
