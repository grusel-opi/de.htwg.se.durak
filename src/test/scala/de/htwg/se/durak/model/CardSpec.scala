package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CardSpec extends WordSpec with Matchers {
  "A card" when {
    val card: Card = Card(CardColor.Hearts, CardValue.Nine)
    "created" should {
      "have a color." in {
        card.color should be(CardColor.Hearts)
      }

      "have a value." in {
        card.value should be(CardValue.Nine)
      }

      "be comparale to cards with same color." in {
        val cardWithSameColorAndLowerValue: Card = Card(CardColor.Hearts,CardValue.Two)
        card.value.compare(cardWithSameColorAndLowerValue.value) should be(1)
      }
    }
  }
}
