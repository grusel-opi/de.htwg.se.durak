package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TurnSpec extends WordSpec with Matchers {
  val attacker: Player = new Player("Fred")
  val victim: Player = new Player("Gertrud")

  val neighborOneOfVictim = attacker
  val neigborTwoOfVictim = new Player("Wolfgang")

  val trumpCard: Card = Card(CardColor.Herz, CardValue.Eight)

  // val turnWithEmptyCards: Turn = Turn(attacker, victim, neighborOneOfVictim, neigborTwoOfVictim, Nil, Nil, trumpCard)

  val attackCards: List[Card] = List(Card(CardColor.Herz, CardValue.Five), Card(CardColor.Karo, CardValue.Five))
  // val turnWithDefinedAttackCards: Turn = Turn(attacker, victim, neighborOneOfVictim, neigborTwoOfVictim, attackCards, Nil, trumpCard)

  "A turn" when {
    "created" should {
      "have a attacker." in {
        // turnWithDefinedAttackCards.attacker should be(attacker)
      }

      "have a victim." in {
        // turnWithDefinedAttackCards.victim should be(victim)
      }

      "have neighbors." in {
        // turnWithDefinedAttackCards.neighbor0 should be(neighborOneOfVictim)
        // turnWithDefinedAttackCards.neighbor1 should be(neigborTwoOfVictim)
      }

      "have attack cards." in {
        // turnWithDefinedAttackCards.attackCards should be(attackCards)
      }

      "have block cards." in {
        // turnWithDefinedAttackCards.blockCards should be(Nil)
      }

      "have a trump card." in {
        // turnWithDefinedAttackCards.trumpCard should be(trumpCard)
      }

      "have a nice string representation." in {
        val attackCardsOnTableAsString: String = "Cards on table: " + attackCards + "\n"
        val blockCardsAsString: String = "Blocking cards: " + Nil + "\n"
        val turnStringRepresenation: String = attackCardsOnTableAsString.concat(blockCardsAsString)

        // turnWithDefinedAttackCards.toString should be(turnStringRepresenation)
      }
    }

    "trying to add block card" should {
      "have more block cards as before if blocking card beat one of the attack cards." in {

        val blockingCardOne: Card = Card(CardColor.Hearts, CardValue.Six)
        val blockingCardTwo: Card = Card(CardColor.Spades, CardValue.Six)

        // val newTurnOne: Turn = turnWithDefinedAttackCards.addBlockCard(blockingCardOne)
        // val newTurnTwo: Turn = newTurnOne.addBlockCard(blockingCardTwo)

        // newTurnOne.blockCards.size should be(1)
        // newTurnOne.blockCards(0) should be(blockingCardOne)

        // newTurnTwo.blockCards.size should be(2)
        // newTurnTwo.blockCards(0) should be(blockingCardTwo)
        // newTurnTwo.blockCards(1) should be(blockingCardOne)
      }

      "have the same amount of block cards if no attack card can be beaten." in {
        val loosingBlockingCard: Card = Card(CardColor.Clubs, CardValue.Two)

        // val turn: Turn = turnWithDefinedAttackCards.addBlockCard(loosingBlockingCard)

        // turn.blockCards.size should be(turnWithDefinedAttackCards.blockCards.size)
      }
    }

    "trying to check if attack card is a valid card to lay" should {
      "return false when attack card have not the same card value than the others." in {
        val attackCardWithHigherValue: Card = Card(CardColor.Hearts, CardValue.Jack)

        // turnWithDefinedAttackCards.checkAttackCard(attackCardWithHigherValue) should be(false)
      }

      "return true when no attack card exists so far." in {
        val attackCard: Card = Card(CardColor.Hearts, CardValue.Two)

        // turnWithEmptyCards.checkAttackCard(attackCard) should be(true)
      }

      "return true when attack card have the same value than the others." in {
        val attackCardWithSameValue: Card = Card(CardColor.Clubs, CardValue.Five)

        // turnWithDefinedAttackCards.checkAttackCard(attackCardWithSameValue) should be(true)
      }
    }

    "trying to add attack card" should {
      val attackCardOne: Card = Card(CardColor.Diamond, CardValue.Ace)
      val attackCardTwo: Card = Card(CardColor.Spades, CardValue.Ace)
      val invadlidAttackCard: Card = Card(CardColor.Hearts, CardValue.Queen)

      // val turnWithOneAttackCard: Turn = turnWithEmptyCards.addAttackCard(attackCardOne)
      // val turnWithTwoAttackCards: Turn = turnWithOneAttackCard.addAttackCard(attackCardTwo)
      // val turnWithTwoAttackCardsAndOneInvalidCard: Turn = turnWithTwoAttackCards.addAttackCard(invadlidAttackCard)

      "add a card if attack cards are empty" in {
        // turnWithEmptyCards.attackCards.size should be(0)
        // turnWithOneAttackCard.attackCards.size should be(1)
      }

      "add another attack card, if it has the same value than the other attack cards." in {
        // turnWithTwoAttackCards.attackCards.size should be(2)
      }

      "not add another attack card, if it has not the same value than the other attack cards." in {
        // turnWithTwoAttackCardsAndOneInvalidCard.attackCards.size should be(2)
      }
    }

    "trying to add block card" should {
      "add a card when the blocking card beats one of the attack cards." in {

      }
    }

    "trying to check if a block card beats any of the attack cards" should {
      // val turnWithOneAttackCard: Turn = turnWithEmptyCards.addAttackCard(Card(CardColor.Diamond, CardValue.Five))

      "return true when the block card have the same color as one of the attack cards and higher value." in {
        val blockCard: Card = Card(CardColor.Diamond, CardValue.Six)

        // turnWithOneAttackCard.checkBlockCard(blockCard) should be(true)
      }

      "return false when the block card have the same color as one of the attack cards and lower value." in {
        val blockCard: Card = Card(CardColor.Diamond, CardValue.Four)

        // turnWithOneAttackCard.checkBlockCard(blockCard) should be(false)
      }

      "return false when the block card have other color as one of the attack cards and have different color from trump " +
        "card." in {
        val blockCard: Card = Card(CardColor.Clubs, CardValue.Ace)

        // turnWithOneAttackCard.checkBlockCard(blockCard) should be(false)
      }

      "return true if block card have same color than trump card and diffrent color from attackig cards." in {
        val blockCard: Card = Card(CardColor.Hearts, CardValue.Three)

        // turnWithOneAttackCard.checkBlockCard(blockCard) should be(true)
      }

      "return true when the block card and one of the attacking cards have same color as trump card, but the blocking " +
        "card have a higher value then the attacking cards." in {
        val attackCard: Card = Card(CardColor.Hearts, CardValue.Seven)
        val blockCard: Card = Card(CardColor.Hearts, CardValue.Ace)

        // val trunWithOneTrumpAttackCard: Turn = turnWithEmptyCards.addAttackCard(attackCard)

        // trunWithOneTrumpAttackCard.checkBlockCard(blockCard) should be(true)
      }
    }

    "a player trying to add a card" should {
      "add a block card (if valid) when the player is victim." in {
        val validBlockCard: Card = Card(CardColor.Hearts, CardValue.Ace)
        // val turn: Turn = turnWithDefinedAttackCards.addCard(victim, validBlockCard)

        // turn.blockCards.size should be(turnWithDefinedAttackCards.blockCards.size + 1)
      }

      "add a attack card (if valid) when the player is attacker." in {
        val validAttackCard: Card = Card(CardColor.Diamond, CardValue.Four)

        // val turn: Turn = turnWithEmptyCards.addCard(attacker, validAttackCard)

        // turn.attackCards.size should be(turnWithEmptyCards.attackCards.size + 1)
      }

      "add no card if the blocking card is not valid." in {
        val invalidAttackCard: Card = Card(CardColor.Clubs, CardValue.Three)
        val invalidBlockCard: Card = Card(CardColor.Spades, CardValue.Six)

        // val turnWithOneAttackCard: Turn = turnWithEmptyCards.addAttackCard(Card(CardColor.Clubs, CardValue.Ten))
        // val attackerTurn: Turn = turnWithOneAttackCard.addCard(attacker, invalidAttackCard)

        // val victimTurn: Turn = turnWithOneAttackCard.addCard(victim, invalidBlockCard)

        // turnWithOneAttackCard.attackCards.size should be(1)
        // turnWithOneAttackCard.attackCards(0) should be (Card(CardColor.Clubs, CardValue.Ten))

        // attackerTurn.attackCards.size should be(1)

        // victimTurn.blockCards.size should be(0)
      }
    }
  }
}
