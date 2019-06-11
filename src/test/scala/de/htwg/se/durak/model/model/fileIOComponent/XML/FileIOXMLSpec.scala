package de.htwg.se.durak.model.model.fileIOComponent.XML

import java.io.FileNotFoundException

import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.deckComponent.DeckInterface
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.Game
import de.htwg.se.durak.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.durak.model.turnComponent.TurnInterface
import de.htwg.se.durak.model.turnComponent.turnBaseImpl.Turn
import de.htwg.se.durak.model.fileIOComponent.fileIOXmlImpl.FileIO
import de.htwg.se.durak.model.playerComponent.PlayerInterface
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
    val karo_fünf = Card(CardColor.Karo, CardValue.Fünf)
    val player1_handcards: List[CardInterface] = List(pik_acht, herz_sieben, karo_ass, kreuz_dame)
    val attackcards = List(karo_fünf)

    val herz_fünf = Card(CardColor.Herz, CardValue.Fünf)
    val kreuz_acht = Card(CardColor.Kreuz, CardValue.Acht)
    val kreuz_könig = Card(CardColor.Kreuz, CardValue.König)
    val karo_zwei = Card(CardColor.Karo, CardValue.Zwei)
    val pik_ass = Card(CardColor.Pik, CardValue.Ass)
    val herz_zwei = Card(CardColor.Herz, CardValue.Zwei)
    val herz_drei = Card(CardColor.Herz, CardValue.Zwei)
    val player_2_handcards: List[CardInterface] = List(herz_fünf, kreuz_acht, kreuz_könig, karo_zwei, pik_ass)
    val blockingcards: Map[CardInterface, CardInterface] = Map(herz_zwei -> herz_drei)

    val player1 = Player("Abduhl", player1_handcards)
    val player2 = Player("Alfred", player_2_handcards)
    val players_list = List(player1, player2)

    val deck_card = Card(CardColor.Herz, CardValue.Ass)
    val deck: DeckInterface = Deck(List(deck_card))

    val trump_card = Card(CardColor.Kreuz, CardValue.Drei)


    val turn: TurnInterface = Turn(player1, player2, player2, attackcards, blockingcards)

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
          <attackCards>
            <card>
              <color>Karo</color>
              <value>Fünf</value>
            </card>
          </attackCards>
          <blockedBy>
            <attackCards>
              <card>
                <color>Herz</color>
                <value>Zwei</value>
              </card>
            </attackCards>
            <blockingCards>
              <card>
                <color>Herz</color>
                <value>Zwei</value>
              </card>
            </blockingCards>
          </blockedBy>
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
      val location = "save/" + file_name_with_extension

      var file: Option[BufferedSource] = None

      try {
        file = Some(Source.fromFile(location))
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
      val loaded_game = fileIO.load(file_name)

      "produce the expected durak game" in {
        loaded_game.players should be(game.players)
        loaded_game.active should be(game.players.head)
        loaded_game.currentTurn should be(game.currentTurn)
        loaded_game.deck should be(game.deck)
        loaded_game.winners should be(game.winners)
        loaded_game.trump should be(game.trump)
        loaded_game.getNeighbour(player1) should be(game.players(1))
        loaded_game.getNeighbour(player2) should be(game.players.head)
      }

    }

    "parsed to xml" should {
      "produce the expected durak game as xml" in {
        fileIO.gameToXml(game).toString().replaceAll("\\s", "").equals(
          expected_xml_content.toString().replaceAll("\\s", "")) should be(true)
      }
    }

    "creating the winners list" should {
      val new_player1 = Player("Abduhl", List())
      val players: List[PlayerInterface] = List(new_player1, player2)
      val game_with_winner = Game(players, deck, trump_card, turn, player1, List(player1))

      "return a valid winners list" in {
        val xml_content = fileIO.gameToXml(game_with_winner)
        fileIO.createWinnersList(xml_content) should be(List(new_player1))
      }
    }
  }
}
