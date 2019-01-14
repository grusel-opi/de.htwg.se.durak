package de.htwg.se.durak.model.fileIOComponent.fileIOXmlImpl

import java.io.{File, PrintWriter}

import de.htwg.se.durak.model.fileIOComponent.FileIOInterface
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.{Game, Turn}
import de.htwg.se.durak.model.playerComponent.Player
import de.htwg.se.durak.util.cardConverter.CardStringConverter

import scala.xml.Node
import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface {

  override def load(fileName: String): GameInterface = {
    var game: GameInterface = null
    var players: List[Player] = Nil
    var deck: Deck = null
    var trump: Card = null
    var currentTurn: Turn = null
    var active: Player = null
    var ok: List[Player] = Nil
    var winner: Option[Player] = None

    val fileNameWithoutExtension: String = removeExtensionFromFileName(fileName)
    val file = xml.XML.loadFile("save/" + fileNameWithoutExtension + ".xml")

    val playersNode = (file \ "players" \\ "player")
    val deckNode = (file \\ "deck")
    val trumpNode = (file \\ "trump")
    val currentTurnNode = (file \\ "turn")
    val activeNode = (file \\ "active")
    val okNode = (file \\ "ok")
    val winnerNode = (file \\ "winner")

    playersNode.foreach(node => {
      val name = (node \ "name").text.toString.trim
      var handCards: List[Card] = Nil

      (node \ "handCards" \ "card").foreach(c => {
        handCards = handCards ::: createCard(c)
      })

      val player: Player = Player(name, handCards)
      players = players ::: List(player)
    })

    var deckCards: List[Card] = Nil

    deckNode.foreach(node => {
      (node \ "card").foreach(c => {
        deckCards = deckCards ::: createCard(c)
      })
    })
    deck = Deck(deckCards)

    (trumpNode \ "card").foreach(c => {
      trump = createCard(c).head
    })

    val attackerName = (currentTurnNode \ "attacker" \ "player" \ "name").text.toString.trim
    val victimName = (currentTurnNode \ "victim" \ "player" \ "name").text.toString.trim
    val neighbourName = (currentTurnNode \ "neighbour" \ "player" \ "name").text.toString.trim
    var attackCards: List[Card] = Nil

    (currentTurnNode \ "attackCards" \ "card").foreach(c => {
      attackCards = attackCards ::: createCard(c)
    })

    var blockedAttackCards: List[Card] = Nil

    (currentTurnNode \ "blockedBy" \ "attackCards" \ "card").foreach(c => {
      blockedAttackCards = blockedAttackCards ::: createCard(c)
    })

    var blockingCards: List[Card] = Nil
    (currentTurnNode \ "blockedBy" \ "blockingCards" \ "card").foreach(c => {
      blockingCards = blockingCards ::: createCard(c)
    })

    val attacker: Player = players.filter(p => p.name.equals(attackerName)).head
    val victim: Player = players.filter(p => p.name.equals(victimName)).head
    val neighbour: Player = players.filter(p => p.name.equals(neighbourName)).head
    var blockedBy: Map[Card, Card] = Map()

    for ((attackCard, blockingCard) <- (blockedAttackCards zip blockingCards)) {
      blockedBy = blockedBy + (attackCard -> blockingCard)
    }

    currentTurn = Turn(attacker, victim, neighbour, attackCards, blockedBy)

    val activePlayerName: String = (activeNode \ "player" \ "name").text.toString.trim
    active = players.filter(p => p.name.equals(activePlayerName)).head

    // TODO: set ok and winner!

    Game(players, deck, trump, currentTurn, active, ok, winner)
  }

  def createCard(c: Node): List[Card] = {
    val cardColor: String = (c \ "color").text.trim
    val cardValue: String = (c \ "value").text.trim
    val card: Card = CardStringConverter.parseCardStringToCardObject(cardColor + " " + cardValue)
    List(card)
  }

  override def save(game: GameInterface, fileName: String): Unit = saveString(game, fileName)

  def saveString(game: GameInterface, fileName: String): Unit = {
    val fileNameWithoutExtension: String = removeExtensionFromFileName(fileName)
    val printWriter: PrintWriter = new PrintWriter(new File("save/" + fileNameWithoutExtension + ".xml"))
    val prettyPrinter: PrettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameToXml(game))
    printWriter.write(xml)
    printWriter.close()
  }

  def removeExtensionFromFileName(fileName: String): String = {
    if (fileName.contains(".")) {
      fileName.substring(0, fileName.lastIndexOf('.'))
    } else {
      fileName
    }
  }

  def gameToXml(game: GameInterface): Elem = {
    println(game.ok.map(p => p.nameToXml))

    if (game.winner.isDefined) {
      <game>
        <players>
          {game.players.map(p => p.toXml)}
        </players>
        <deck>
          {game.deck.toXml}
        </deck>
        <trump>
          {game.trump.toXml}
        </trump>
        <currentTurn>
          {game.currentTurn.toXml}
        </currentTurn>
        <active>
          {game.active.toXml}
        </active>
        <ok>
          {game.ok.map(p => p.toXml)}
        </ok>
        <winner>
          {game.winner.get.toXml}
        </winner>
      </game>
    } else {
      <game>
        <players>
          {game.players.map(p => p.toXml)}
        </players>
        <deck>
          {game.deck.toXml}
        </deck>
        <trump>
          {game.trump.toXml}
        </trump>
        <currentTurn>
          {game.currentTurn.toXml}
        </currentTurn>
        <active>
          {game.active.nameToXml}
        </active>
        <ok>
          {game.ok.map(p => p.nameToXml)}
        </ok>
        <winner>
        </winner>
      </game>
    }
  }
}
