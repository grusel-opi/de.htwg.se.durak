package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec with Matchers {

  "A Player" when {

    "created with hand cards" should {

      val twoOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Two)
      val threeOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Three)
      val fourOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Four)
      val fiveOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Five)

      val handCards: List[Card] = List(twoOfDiamonds, threeOfDiamonds, fourOfDiamonds, fiveOfDiamonds)

      val playerWithFourHandCards: Player = Player("Hans", handCards)

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

    "created without hand cards" should {

      val playerWithoutHandCards: Player = new Player("Peter")

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

  }


  "A Player with four hand cards" when {

    val twoOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Two)
    val threeOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Three)
    val fourOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Four)
    val fiveOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Five)

    val handCards: List[Card] = List(twoOfDiamonds, threeOfDiamonds, fourOfDiamonds, fiveOfDiamonds)

    val playerWithFourHandCards: Player = new Player("Robin", handCards)

    "picking one card" should {
      "have five hand cards." in {
        val cardToPick: List[Card] = List(Card(CardColor.Spades, CardValue.Three))

        val playerWithFiveHandCards: Player = playerWithFourHandCards.pickCards(cardToPick)

        playerWithFiveHandCards.handCards.size should be(playerWithFourHandCards.handCards.size + 1)
        playerWithFiveHandCards.handCards should be(cardToPick ::: playerWithFourHandCards.handCards)
      }
    }

    "picking five cards" should {
      "have nine hand cards." in {
        val twoOfHearts: Card = Card(CardColor.Hearts, CardValue.Two)
        val twoOfSpades: Card = Card(CardColor.Spades, CardValue.Two)
        val twoOfClubs: Card = Card(CardColor.Clubs, CardValue.Two)
        val aceOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Ace)
        val kingOfSpades: Card = Card(CardColor.Spades, CardValue.King)

        val cardsToPick: List[Card] = List(twoOfHearts, twoOfSpades, twoOfClubs, aceOfDiamonds, kingOfSpades)

        val playerWithNineHandCards: Player = playerWithFourHandCards.pickCards(cardsToPick)

        playerWithNineHandCards.handCards.size should be(playerWithFourHandCards.handCards.size + 5)
        playerWithNineHandCards.handCards should be(cardsToPick ::: playerWithFourHandCards.handCards)
      }
    }

    "drop one card that is a card of his hand cards" should {
      "have three hand cards." in {
        val cardToDrop: List[Card] = List(Card(CardColor.Diamond, CardValue.Five))
        val playerWithThreeHandCards: Player = playerWithFourHandCards.dropCards(cardToDrop)

        playerWithThreeHandCards.handCards.size should be(playerWithFourHandCards.handCards.size - 1)
        playerWithThreeHandCards.handCards should be(playerWithFourHandCards.handCards.filterNot(e => cardToDrop.contains(e)))
      }
    }

    "drop one card that is not a card of his hand cards" should {
      "still have four hand cards." in {
        val twoOfHearts: Card = Card(CardColor.Hearts, CardValue.Two)
        val cardToDrop: List[Card] = List(twoOfHearts)

        val newPlayerWithFourHandCards: Player = playerWithFourHandCards.dropCards(cardToDrop)

        newPlayerWithFourHandCards.handCards.size should be(playerWithFourHandCards.handCards.size)
        newPlayerWithFourHandCards.handCards should be(playerWithFourHandCards.handCards)
      }
    }

    "drop three cards that are cards of his hand cards" should {
      "have one hand" in {
        val cardsToDrop: List[Card] = List(twoOfDiamonds, threeOfDiamonds, fourOfDiamonds)

        val playerWithOneHandCard: Player = playerWithFourHandCards.dropCards(cardsToDrop)

        playerWithOneHandCard.handCards.size should be(playerWithFourHandCards.handCards.size - 3)
        playerWithOneHandCard.handCards should be(playerWithFourHandCards.handCards.filterNot(e => cardsToDrop.contains(e)))
      }
    }

    "trying to determine if he has a specific card" should {
      "return true if he own the card." in {
        playerWithFourHandCards.hasCard(twoOfDiamonds) should be(true)
        playerWithFourHandCards.hasCard(threeOfDiamonds) should be(true)
        playerWithFourHandCards.hasCard(fourOfDiamonds) should be(true)
        playerWithFourHandCards.hasCard(fiveOfDiamonds) should be(true)
      }

      "return false if he doesen't own the card." in {
        val cardThatIsNotACardOfThePlayersHandCards: Card = Card(CardColor.Clubs, CardValue.Ten)
        playerWithFourHandCards.hasCard(cardThatIsNotACardOfThePlayersHandCards) should be(false)
      }
    }
  }


  "A player with one hand card" when {

    val threeOfClubs: Card = Card(CardColor.Clubs, CardValue.Three)
    val handCards: List[Card] = List(threeOfClubs)
    val playerWithOneHandCard: Player = Player("Luigi", handCards)

    "picking one card" should {
      "have two hand cards." in {
        val cardToPick: List[Card] = List(Card(CardColor.Spades, CardValue.Three))

        val playerWithTwoHandCards: Player = playerWithOneHandCard.pickCards(cardToPick)

        playerWithTwoHandCards.handCards.size should be(playerWithOneHandCard.handCards.size + 1)
        playerWithTwoHandCards.handCards should be(cardToPick ::: handCards)
      }
    }

    "picking five cards" should {
      "have six hand cards" in {
        val twoOfHearts: Card = Card(CardColor.Hearts, CardValue.Two)
        val twoOfSpades: Card = Card(CardColor.Spades, CardValue.Two)
        val twoOfClubs: Card = Card(CardColor.Clubs, CardValue.Two)
        val aceOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Ace)
        val kingOfSpades: Card = Card(CardColor.Spades, CardValue.King)

        val cardsToPick: List[Card] = List(twoOfHearts, twoOfSpades, twoOfClubs, aceOfDiamonds, kingOfSpades)

        val playerWithSixHandCards: Player = playerWithOneHandCard.pickCards(cardsToPick)

        playerWithSixHandCards.handCards.size should be(playerWithOneHandCard.handCards.size + 5)
        playerWithSixHandCards.handCards should be(cardsToPick ::: playerWithOneHandCard.handCards)
      }
    }

    "drop one card that is a card of his hand cards" should {
      "have no cards anymore." in {
        val cardToDrop: List[Card] = List(threeOfClubs)
        val playerWithoutHandCards: Player = playerWithOneHandCard.dropCards(cardToDrop)

        playerWithoutHandCards.handCards.size should be(playerWithOneHandCard.handCards.size - 1)
        playerWithoutHandCards.handCards should be(Nil)
      }
    }

    "drop one card that is not a card of his hand cards" should {
      "not drop any of his cards." in {
        val cardToDrop: List[Card] = List(Card(CardColor.Diamond, CardValue.Ten))

        val newPlayerWithOneHandCard: Player = playerWithOneHandCard.dropCards(cardToDrop)

        newPlayerWithOneHandCard.handCards.size should be(playerWithOneHandCard.handCards.size)
        newPlayerWithOneHandCard.handCards should be(playerWithOneHandCard.handCards)
      }
    }
  }


  "A player without hand cards" when {

    val playerWithoutHandCards: Player = new Player("Peter")

    "picking one card" should {
      "have one hand card." in {
        val cardToPick: List[Card] = List(Card(CardColor.Spades, CardValue.Three))

        val playerWithOneHandCard: Player = playerWithoutHandCards.pickCards(cardToPick)

        playerWithOneHandCard.handCards.size should be(playerWithoutHandCards.handCards.size + 1)
        playerWithOneHandCard.handCards should be(cardToPick)
      }
    }

    "picking five cards" should {
      "have five hand cards" in {
        val twoOfHearts: Card = Card(CardColor.Hearts, CardValue.Two)
        val twoOfSpades: Card = Card(CardColor.Spades, CardValue.Two)
        val twoOfClubs: Card = Card(CardColor.Clubs, CardValue.Two)
        val aceOfDiamonds: Card = Card(CardColor.Diamond, CardValue.Ace)
        val kingOfSpades: Card = Card(CardColor.Spades, CardValue.King)

        val cardsToPick: List[Card] = List(twoOfHearts, twoOfSpades, twoOfClubs, aceOfDiamonds, kingOfSpades)

        val playerWithFiveHandCards: Player = playerWithoutHandCards.pickCards(cardsToPick)

        playerWithFiveHandCards.handCards.size should be(playerWithoutHandCards.handCards.size + 5)
        playerWithFiveHandCards.handCards should be(cardsToPick ::: playerWithoutHandCards.handCards)
      }
    }

    "drop one card" should {
      "still have no hand cards." in {
        val fourOfSpades: Card = Card(CardColor.Spades, CardValue.Four)
        val cardToDrop: List[Card] = List(fourOfSpades)

        val newPlayerWithoutHandCards: Player = playerWithoutHandCards.dropCards(cardToDrop)

        newPlayerWithoutHandCards.handCards.size should be(playerWithoutHandCards.handCards.size)
        newPlayerWithoutHandCards.handCards should be(newPlayerWithoutHandCards.handCards)
      }
    }

    "trying to determine if he has a specific card" should {
      "always return false, because he doesn't own any cards." in {
        val twoOfSpades: Card = Card(CardColor.Spades, CardValue.Two)

        playerWithoutHandCards.hasCard(twoOfSpades) should be (false)
      }
    }
  }
}
