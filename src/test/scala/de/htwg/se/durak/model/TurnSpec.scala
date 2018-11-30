package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TurnSpec extends WordSpec with Matchers {
  "A Turn" when {
    val attackCard = Card(CardColor.Herz, CardValue.Two)
    val blockCard = Card(CardColor.Herz, CardValue.Three)
    val turn: Turn = Turn(Player("1", Nil), Player("1", Nil), Player("1", Nil), Nil, Map[Card, Card]())
    "it gets a new attack card" must {
      turn.addAttackCard(attackCard)
      "have it as a attack card: \" + )" in {
        turn.attackCards contains attackCard
      }
    }
    "it gets a new block card" should {
      turn.addBlockCard(attackCard, blockCard)
      "not have it as a attack card any more: \" + )" in {
        turn.blockedBy.keys.toList contains attackCard
        turn.blockedBy.values.toList contains blockCard
      }
    }
  }
}
