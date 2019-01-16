package de.htwg.se.durak.model.model.cardComponent

import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsObject, JsString, Json}

import scala.xml.{Node, Utility}

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

    "parsed to XML" should {
      "return the parsed card as a xml structure." in {
        val cardAsXml: Node = Utility.trim(card.toXml)

        val expectedXmlCardStructure: Node =
          Utility.trim(
            <card>
              <color>
                {CardColor.Herz}
              </color>
              <value>
                {CardValue.Neun}
              </value>
            </card>
          )

        cardAsXml should be(expectedXmlCardStructure)
      }
    }

    "parsed to JSON" should {
      "return the parsed card as a json structure." in {
        val cardAsJson: JsObject = card.toJson

        val expectedJsonCardStructure: JsObject = {
          Json.obj(
            "card" -> Json.obj(
              "color" -> JsString(CardColor.Herz.toString),
              "value" -> JsString(CardValue.Neun.toString)
            )
          )
        }

        cardAsJson should be(expectedJsonCardStructure)
      }
    }
  }
}
