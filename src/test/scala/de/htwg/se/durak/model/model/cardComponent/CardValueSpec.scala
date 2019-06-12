package de.htwg.se.durak.model.model.cardComponent

import de.htwg.se.durak.model.cardComponent.cardBaseImpl.CardValue
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class CardValueSpec extends WordSpec with Matchers {
  "A card value" when {
    val jackCardValue: CardValue.Value = CardValue.Bube

    "compared to a lower card value" should {
      "return compare value 1" in {
        val nineCardValue: CardValue.Value = CardValue.Neun
        jackCardValue.compare(nineCardValue) should be(1)
      }
    }

    "compared to a higher card value" should {
      "return compare value -1" in {
        val aceCardValue: CardValue.Value = CardValue.Ass
        jackCardValue.compare(aceCardValue) should be (-1)
      }
    }

    "compared to the same card value" should {
      "return compare value 0" in {
        jackCardValue.compare(jackCardValue) should be(0)
      }
    }
  }
}