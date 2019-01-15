package de.htwg.se.durak.model

import de.htwg.se.durak.model.cardComponent.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.playerComponent.Player
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec with Matchers {
  "A Player" when {
    "created with hand cards" should {
      val card1: Card = Card(CardColor.Herz, CardValue.Zwei)
      val card2: Card = Card(CardColor.Pik, CardValue.Zehn)
      val handCards: List[Card] = List(card1, card2)

      val player: Player = Player("Hans", handCards)

      "have a name." in {
        player.name should be("Hans")
      }

      "have hand cards." in {
        player.handCards.size should be(handCards.size)
        player.handCards should be(handCards)
      }

      "have a nice string representation." in {
        player.toString should be("Hans")
      }
    }

    "created without hand cards" should {
      val player: Player = new Player("Peter")

      "have a name." in {
        player.name should be("Peter")
      }

      "have empty hand cards." in {
        player.handCards.size should be(0)
        player.handCards should be(Nil)
      }
    }

    "pick a card" should {
      val player: Player = new Player("Abduhl")

      "have one card more as before." in {
        val cardToPick: List[Card] = List(Card(CardColor.Karo, CardValue.Vier))

        player.handCards.size should be(0)
        player.handCards should be(Nil)

        val oldSize: Int = player.handCards.size

        player.pickCards(cardToPick)

        player.handCards.size should be(oldSize + 1)
        player.handCards should be(cardToPick)
      }
    }

    "pick two cards" should {
      val player: Player = new Player("Fred")

      "have two cards more as before." in {
        val cardsToPick: List[Card] = List(Card(CardColor.Kreuz, CardValue.Bube), Card(CardColor.Herz, CardValue.Acht))

        player.handCards.size should be(0)
        player.handCards should be(Nil)

        val oldSize: Int = player.handCards.size

        player.pickCards(cardsToPick)

        player.handCards.size should be(oldSize + 2)
        player.handCards should be(cardsToPick)
      }
    }

    "drop a card" should {
      val card1: Card = Card(CardColor.Herz, CardValue.Sieben)
      val card2: Card = Card(CardColor.Karo, CardValue.Bube)
      val handCards: List[Card] = List(card1, card2)

      val player: Player = Player("Gabriel", handCards)

      "have one card less as before." in {
        player.handCards.size should be(handCards.size)
        player.handCards should be(handCards)

        val cardToDrop: List[Card] = List(card1)
        val oldSize: Int = player.handCards.size

        player.dropCards(cardToDrop)

        player.handCards.size should be(oldSize - 1)
        player.handCards should be(List(card2))
      }
    }

    "drop two cards" should {
      val card1: Card = Card(CardColor.Herz, CardValue.Zehn)
      val card2: Card = Card(CardColor.Karo, CardValue.König)
      val handCards: List[Card] = List(card1, card2)

      val player: Player = Player("Hannes", handCards)

      "have two cards less as before." in {
        player.handCards.size should be(handCards.size)
        player.handCards should be(handCards)

        val cardsToDrop: List[Card] = List(card1, card2)
        val oldSize: Int = player.handCards.size
        player.dropCards(cardsToDrop)

        player.handCards.size should be(oldSize - 2)
        player.handCards should be(Nil)
      }
    }

    "try to determine if he has a specific card" should {
      val cardThatPlayerOwns: Card = Card(CardColor.Herz, CardValue.Ass)
      val cardThatPlayerDoesntOwn: Card = Card(CardColor.Karo, CardValue.Ass)
      val handCards: List[Card] = List(cardThatPlayerOwns)
      val player: Player = Player("Martin", handCards)

      "be true if he owns the card." in {
        player.handCards.size should be(handCards.size)
        player.handCards should be(handCards)
        player.hasCard(cardThatPlayerOwns) should be(true)
      }

      "be false if he doesn't own the card." in {
        player.handCards.size should be(handCards.size)
        player.handCards should be(handCards)
        player.hasCard(cardThatPlayerDoesntOwn) should be(false)
      }
    }
  }
}
