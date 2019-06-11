package de.htwg.se.durak.model.model.fileIOComponent.XML

import java.io.FileNotFoundException

import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.deckComponent.DeckInterface
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.fileIOComponent.FileIOInterface
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.Game
import de.htwg.se.durak.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.durak.model.turnComponent.TurnInterface
import de.htwg.se.durak.model.turnComponent.turnBaseImpl.Turn
import de.htwg.se.durak.model.fileIOComponent.fileIOXmlImpl.FileIO
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

import scala.io.{BufferedSource, Source}
import scala.xml.Elem

@RunWith(classOf[JUnitRunner])
class FileIOXMLSpec extends WordSpec with Matchers {
  "A Game" when {

    val pik_acht = Card(CardColor.Pik, CardValue.Acht)
    val herz_sieben = Card(CardColor.Herz, CardValue.Sieben)
    val karo_ass = Card(CardColor.Karo, CardValue.Ass)
    val kreuz_dame = Card(CardColor.Kreuz, CardValue.Dame)
    val player1_handcards: List[CardInterface] = List(pik_acht, herz_sieben, karo_ass, kreuz_dame)

    val herz_fünf = Card(CardColor.Herz, CardValue.Fünf)
    val kreuz_acht = Card(CardColor.Kreuz, CardValue.Acht)
    val kreuz_könig = Card(CardColor.Kreuz, CardValue.König)
    val karo_zwei = Card(CardColor.Karo, CardValue.Zwei)
    val pik_ass = Card(CardColor.Pik, CardValue.Ass)
    val player_2_handcards: List[CardInterface] = List(herz_fünf, kreuz_acht, kreuz_könig, karo_zwei, pik_ass)

    val player1 = Player("Abduhl", player1_handcards)
    val player2 = Player("Alfred", player_2_handcards)
    val players_list = List(player1, player2)

    val deck_card = Card(CardColor.Herz, CardValue.Ass)
    val deck: DeckInterface = Deck(List(deck_card))

    val trump_card = Card(CardColor.Kreuz, CardValue.Drei)

    val turn: TurnInterface = new Turn(player1, player2, player2)

    val game = Game(players_list, deck, trump_card, turn, player1, List())

    val fileIO: FileIO = new FileIO()

    val file_name = "test"
    val file_name_with_extension = file_name + ".xml"

    val expected_xml_content: Elem =
      <game>
        <players>
          <player>
            <name>Abduhl</name>
            <handCards>
              <card>
                <color>Pik</color>
                <value>Acht</value>
              </card>
              <card>
                <color>Herz</color>
                <value>Sieben</value>
              </card>
              <card>
                <color>Karo</color>
                <value>Ass</value>
              </card>
              <card>
                <color>Kreuz</color>
                <value>Dame</value>
              </card>
            </handCards>
          </player>
          <player>
            <name>Alfred</name>
            <handCards>
              <card>
                <color>Herz</color>
                <value>Fünf</value>
              </card>
              <card>
                <color>Kreuz</color>
                <value>Acht</value>
              </card>
              <card>
                <color>Kreuz</color>
                <value>König</value>
              </card>
              <card>
                <color>Karo</color>
                <value>Zwei</value>
              </card>
              <card>
                <color>Pik</color>
                <value>Ass</value>
              </card>
            </handCards>
          </player>
        </players>
        <deck>
          <card>
            <color>Herz</color>
            <value>Ass</value>
          </card>
        </deck>
        <trump>
          <card>
            <color>Kreuz</color>
            <value>Drei</value>
          </card>
        </trump>
        <currentTurn>
          <attacker>
            <player>
              <name>Abduhl</name>
            </player>
          </attacker>
          <victim>
            <player>
              <name>Alfred</name>
            </player>
          </victim>
          <neighbour>
            <player>
              <name>Alfred</name>
            </player>
          </neighbour>
          <attackCards></attackCards>
        </currentTurn>
        <active>
          <player>
            <name>Abduhl</name>
          </player>
        </active>
        <winners></winners>
      </game>

    "saved" should {
      fileIO.save(game, file_name_with_extension)
      val filename = "save/" + file_name_with_extension

      var file: Option[BufferedSource] = None

      try {
        file = Some(Source.fromFile(filename))
      } catch {
        case fnfe: FileNotFoundException => file = None

      }

      var expected_content = false

      if (file.isDefined) {
        val snipped_expected_content = expected_xml_content.mkString.replaceAll("\\s", "")
        val snipped_file_content = file.get.mkString.replaceAll("\\s", "")

        expected_content = snipped_expected_content.equals(snipped_file_content)

      }

      "have a filename" in {
        file should not be None
      }

      "remove extensions from file name" in {
        fileIO.removeExtensionFromFileName(file_name) should be(file_name)
        fileIO.removeExtensionFromFileName(file_name_with_extension) should be(file_name)
      }

      "produce a savefile" in {
        expected_content should be(true)
      }

    }

    "loaded" should {
      val game = fileIO.load(file_name)

      "produce the expected durak game" in {
        game.players should be(players_list)
        game.active should be(player1)
        game.currentTurn should be(turn)
        game.deck should be(deck)
        game.winners should be(List())
        game.trump should be(trump_card)
        game.getNeighbour(player1) should be(player2)
        game.getNeighbour(player2) should be(player1)
      }

    }

    "parsed to xml " should {
      "produce the expected durak game as xml" in {
        fileIO.gameToXml(game).toString().replaceAll("\\s", "").equals(
          expected_xml_content.toString().replaceAll("\\s", "")) should be(true)
      }
    }
  }
}
