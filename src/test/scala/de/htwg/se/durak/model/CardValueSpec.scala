package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CardValueSpec extends WordSpec with Matchers {
  "A card value" when {
    val jackCardValue: CardValue.Value = CardValue.Jack

    "compared to a lower card value" should {
      "return compare value 1" in {
        val nineCardValue: CardValue.Value = CardValue.Nine
        jackCardValue.compare(nineCardValue) should be(1)
      }
    }

    "compared to a higher card value" should {
      "return compare value -1" in {
        val aceCardValue: CardValue.Value = CardValue.Ace
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