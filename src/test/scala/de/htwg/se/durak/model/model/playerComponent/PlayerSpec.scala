package de.htwg.se.durak.model.model.playerComponent

import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.playerComponent.PlayerInterface
import de.htwg.se.durak.model.playerComponent.playerBaseImpl.Player
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsObject, JsString, Json}

import scala.xml.{Node, Utility}

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec with Matchers {
  "A Player" when {
    "created with hand cards" should {
      val card1: CardInterface = Card(CardColor.Herz, CardValue.Zwei)
      val card2: CardInterface = Card(CardColor.Pik, CardValue.Zehn)
      val handCards: List[CardInterface] = List(card1, card2)

      val player: PlayerInterface = Player("Hans", handCards)

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
      val player: PlayerInterface = new Player("Peter")

      "have a name." in {
        player.name should be("Peter")
      }

      "have empty hand cards." in {
        player.handCards.size should be(0)
        player.handCards should be(Nil)
      }
    }

    "pick a card" should {
      val player: PlayerInterface = new Player("Abduhl")

      "have one card more as before." in {
        val cardToPick: List[CardInterface] = List(Card(CardColor.Karo, CardValue.Vier))

        player.handCards.size should be(0)
        player.handCards should be(Nil)

        val oldSize: Int = player.handCards.size

        player.pickCards(cardToPick)

        player.handCards.size should be(oldSize + 1)
        player.handCards should be(cardToPick)
      }
    }

    "pick two cards" should {
      val player: PlayerInterface = new Player("Fred")

      "have two cards more as before." in {
        val cardsToPick: List[CardInterface] = List(Card(CardColor.Kreuz, CardValue.Bube), Card(CardColor.Herz, CardValue.Acht))

        player.handCards.size should be(0)
        player.handCards should be(Nil)

        val oldSize: Int = player.handCards.size

        player.pickCards(cardsToPick)

        player.handCards.size should be(oldSize + 2)
        player.handCards should be(cardsToPick)
      }
    }

    "drop a card" should {
      val card1: CardInterface = Card(CardColor.Herz, CardValue.Sieben)
      val card2: CardInterface = Card(CardColor.Karo, CardValue.Bube)
      val handCards: List[CardInterface] = List(card1, card2)

      val player: PlayerInterface = Player("Gabriel", handCards)

      "have one card less as before." in {
        player.handCards.size should be(handCards.size)
        player.handCards should be(handCards)

        val cardToDrop: List[CardInterface] = List(card1)
        val oldSize: Int = player.handCards.size

        player.dropCards(cardToDrop)

        player.handCards.size should be(oldSize - 1)
        player.handCards should be(List(card2))
      }
    }

    "drop two cards" should {
      val card1: CardInterface = Card(CardColor.Herz, CardValue.Zehn)
      val card2: CardInterface = Card(CardColor.Karo, CardValue.König)
      val handCards: List[CardInterface] = List(card1, card2)

      val player: PlayerInterface = Player("Hannes", handCards)

      "have two cards less as before." in {
        player.handCards.size should be(handCards.size)
        player.handCards should be(handCards)

        val cardsToDrop: List[CardInterface] = List(card1, card2)
        val oldSize: Int = player.handCards.size
        player.dropCards(cardsToDrop)

        player.handCards.size should be(oldSize - 2)
        player.handCards should be(Nil)
      }
    }

    "try to determine if he has a specific card" should {
      val cardThatPlayerOwns: CardInterface = Card(CardColor.Herz, CardValue.Ass)
      val cardThatPlayerDoesntOwn: CardInterface = Card(CardColor.Karo, CardValue.Ass)
      val handCards: List[CardInterface] = List(cardThatPlayerOwns)
      val player: PlayerInterface = Player("Martin", handCards)

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

    "parsed to XML" should {
      val playerName: String = "Martin"
      val cardThatPlayerOwns: CardInterface = Card(CardColor.Herz, CardValue.Ass)
      val cardThatPlayerDoesntOwn: CardInterface = Card(CardColor.Karo, CardValue.Ass)
      val handCards: List[CardInterface] = List(cardThatPlayerOwns)
      val player: PlayerInterface = Player(playerName, handCards)

      "return the player as a XML structure." in {
        val playerAsXml: Node = Utility.trim(player.toXml)
        val expectedXmlPlayerStructure: Node =
          Utility.trim(
            <player>
              <name>
                {playerName}
              </name>
              <handCards>
                {handCards.map(c => c.toXml)}
              </handCards>
            </player>
          )

        playerAsXml should be(expectedXmlPlayerStructure)
      }
    }

    "the name parsed to XML" should {
      val playerName: String = "Martin"
      val cardThatPlayerOwns: CardInterface = Card(CardColor.Herz, CardValue.Ass)
      val cardThatPlayerDoesntOwn: CardInterface = Card(CardColor.Karo, CardValue.Ass)
      val handCards: List[CardInterface] = List(cardThatPlayerOwns)
      val player: PlayerInterface = Player(playerName, handCards)

      "return the player without hand cards as a XML structure." in {
        val playerAsXml: Node = Utility.trim(player.nameToXml)
        val expectedXmlPlayerStructure: Node = Utility.trim(
          <player>
            <name>
              {playerName}
            </name>
          </player>
        )

        playerAsXml should be(expectedXmlPlayerStructure)
      }
    }

    "parsed to JSON" should {
      val playerName: String = "Martin"
      val cardThatPlayerOwns: CardInterface = Card(CardColor.Herz, CardValue.Ass)
      val cardThatPlayerDoesntOwn: CardInterface = Card(CardColor.Karo, CardValue.Ass)
      val handCards: List[CardInterface] = List(cardThatPlayerOwns)
      val player: PlayerInterface = Player(playerName, handCards)

      "return the player as a JSON structure." in {
        val playerAsJson: JsObject = player.toJson
        val expectedJsonPlayerStructure: JsObject =
          Json.obj(
            "player" -> Json.obj(
              "name" -> JsString(playerName),
              "handCards" -> handCards.map(c => c.toJson)
            )
          )

        playerAsJson should be(expectedJsonPlayerStructure)
      }
    }

    "the name parsed to JSON" should {
      val playerName: String = "Martin"
      val cardThatPlayerOwns: CardInterface = Card(CardColor.Herz, CardValue.Ass)
      val cardThatPlayerDoesntOwn: CardInterface = Card(CardColor.Karo, CardValue.Ass)
      val handCards: List[CardInterface] = List(cardThatPlayerOwns)
      val player: PlayerInterface = Player(playerName, handCards)

      "return the player without hand cards as a XML structure." in {
        val playerAsJson: JsObject = player.nameToJson
        val expectedJsonPlayerStructure: JsObject =
          Json.obj(
            "player" -> Json.obj(
              "name" -> JsString(playerName)
            )
          )

        playerAsJson should be(expectedJsonPlayerStructure)
      }
    }
  }
}
