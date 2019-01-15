package de.htwg.se.durak.model

import de.htwg.se.durak.model.cardComponent.{Card, CardColor, CardValue}
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CardSpec extends WordSpec with Matchers {
  "A card" when {
    val card: Card = Card(CardColor.Herz, CardValue.Neun)
    "created" should {
      "have a color." in {
        card.color should be(CardColor.Herz)
      }

      "have a value." in {
        card.value should be(CardValue.Neun)
      }

      "be comparale to cards with same color." in {
        val cardWithSameColorAndLowerValue: Card = Card(CardColor.Herz, CardValue.Zwei)
        card.value.compare(cardWithSameColorAndLowerValue.value) should be(1)
      }
    }
  }
}
