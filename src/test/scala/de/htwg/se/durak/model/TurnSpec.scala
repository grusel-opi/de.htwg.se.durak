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

  val attackCards: List[Card] = List(Card(CardColor.Hearts, CardValue.Five), Card(CardColor.Spades, CardValue.Five))
  val blockCards: List[Card] = Nil

  val turn: Turn = Turn(attacker, victim, neighborOneOfVictim, neigborTwoOfVictim, attackCards, blockCards)

  "A turn" when {
    "created" should {
      "have a attacker." in {
        turn.attacker should be(attacker)
      }

      "have a victim." in {
        turn.victim should be(victim)
      }

      "have neighbors." in {
        turn.neighbor0 should be(neighborOneOfVictim)
        turn.neighbor1 should be(neigborTwoOfVictim)
      }

      "have attack cards." in {
        turn.attackCards should be(attackCards)
      }

      "have block cards." in {
        turn.blockCards should be(blockCards)
      }

      "have a nice string representation." in {
        val attackCardsOnTableAsString: String = "Cards on table: " + attackCards + "\n"
        val blockCardsAsString: String = "Blocking cards: " + blockCards + "\n"
        val turnStringRepresenation: String = attackCardsOnTableAsString.concat(blockCardsAsString)

        turn.toString should be(turnStringRepresenation)
      }
    }

    "trying to add block card" should {
      "have more block cards as before if blocking card beat one of the attack cards." in {

        val blockingCardOne: Card = Card(CardColor.Hearts, CardValue.Six)
        val blockingCardTwo: Card = Card(CardColor.Spades, CardValue.Six)

        val newTurnOne: Turn = turn.addBlockCard(blockingCardOne)
        val newTurnTwo: Turn = newTurnOne.addBlockCard(blockingCardTwo)

        newTurnOne.blockCards.size should be(1)
        newTurnOne.blockCards(0) should be(blockingCardOne)

        newTurnTwo.blockCards.size should be(2)
        newTurnTwo.blockCards(0) should be(blockingCardTwo)
        newTurnTwo.blockCards(1) should be(blockingCardOne)
      }
    }

    "trying to check if attack card is a valid card to lay" should {
      "return false when attack card have not the same card value than the others." in {
        val attackCardWithHigherValue: Card = Card(CardColor.Hearts, CardValue.Jack)

        turn.checkAttackCard(attackCardWithHigherValue) should be(false)
      }

      "return true when attack card have the same value than the others." in {
        val attackCardWithSameValue: Card = Card(CardColor.Clubs, CardValue.Five)

        turn.checkAttackCard(attackCardWithSameValue) should be(true)
      }
    }
  }
}
