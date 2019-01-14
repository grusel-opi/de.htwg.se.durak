package de.htwg.se.durak.model.fileIOComponent.fileIOJsonImpl

import java.io.{File, PrintWriter}

import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.fileIOComponent.FileIOInterface
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.{Game, Turn}
import de.htwg.se.durak.model.playerComponent.Player
import de.htwg.se.durak.util.cardConverter.CardStringConverter
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.io.Source
import scala.xml.Node

class FileIO extends FileIOInterface {
  override def load(fileName: String): GameInterface = {
    var game: GameInterface = null
    var players: List[Player] = Nil
    var deck: Deck = null
    var trump: Card = null
    var currentTurn: Turn = null
    var active: Player = null
    var ok: List[Player] = Nil
    var winner: List[Player] = Nil

    val fileNameWithoutExtension: String = removeExtensionFromFileName(fileName)
    val sourceOfFile: String = Source.fromFile("save/" + fileNameWithoutExtension + ".json").getLines().mkString

    val sourceOfFileAsJson: JsValue = Json.parse(sourceOfFile)

    val gameNode: JsValue = (sourceOfFileAsJson \ "game").get
    val currentTurnNode: JsValue = (gameNode \ "currentTurn").get
    val activeNode: JsValue = (gameNode \ "active").get
    val okNode: JsValue = (gameNode \ "ok").get
    // val winnerNode: JsValue = (gameNode \ "winner").get

    (sourceOfFileAsJson \ "game" \ "players" \\ "player").foreach(p => {
      val name: String = (p \ "name").get.as[String]
      var handCards: List[Card] = Nil

      (p \ "handCards" \\ "card").foreach(c => {
        handCards = handCards ::: createCard(c)
      })

      val player: Player = Player(name, handCards)
      players = players ::: List(player)
    })

    var deckCards: List[Card] = Nil

    (sourceOfFileAsJson \ "game" \ "deck" \\ "card").foreach(c => {
      deckCards = deckCards ::: createCard(c)
    })

    deck = Deck(deckCards)

    trump = createCard((sourceOfFileAsJson \ "game" \ "trump" \\ "card").head).head

    val attackerName = (sourceOfFileAsJson \ "game" \ "currentTurn" \ "attacker" \ "player" \ "name").get.as[String]
    val victimName = (sourceOfFileAsJson \ "game" \ "currentTurn" \ "victim" \ "player" \ "name").get.as[String]
    val neighbourName = (sourceOfFileAsJson \ "game" \ "currentTurn" \ "neighbour" \ "player" \ "name").get.as[String]
    var attackCards: List[Card] = Nil

    (sourceOfFileAsJson \ "game" \ "currentTurn" \ "attackCards" \\ "card").foreach(c => {
      attackCards = attackCards ::: createCard(c)
    })

    var blockedAttackCards: List[Card] = Nil

    (sourceOfFileAsJson \ "game" \ "currentTurn" \ "blockedBy" \\ "attackCards").foreach(entry => {
      (entry \\ "card").foreach(c => {
        blockedAttackCards = blockedAttackCards ::: createCard(c)
      })
    })

    var blockingCards: List[Card] = Nil

    (sourceOfFileAsJson \ "game" \ "currentTurn" \ "blockedBy" \\ "blockingCards").foreach(entry => {
      (entry \\ "card").foreach(c => {
        blockingCards = blockingCards ::: createCard(c)
      })
    })

    val attacker: Player = players.filter(p => p.name.equals(attackerName)).head
    val victim: Player = players.filter(p => p.name.equals(victimName)).head
    val neighbour: Player = players.filter(p => p.name.equals(neighbourName)).head
    var blockedBy: Map[Card, Card] = Map()

    for ((attackCard, blockingCard) <- (blockedAttackCards zip blockingCards)) {
      blockedBy = blockedBy + (attackCard -> blockingCard)
    }

    currentTurn = Turn(attacker, victim, neighbour, attackCards, blockedBy)

    val activePlayerName: String = (sourceOfFileAsJson \ "game" \ "active" \ "player" \ "name").get.as[String]
    active = players.filter(p => p.name.equals(activePlayerName)).head

    // TODO: set ok and winner!

    Game(players, deck, trump, currentTurn, active, winner)
  }

  override def save(game: GameInterface, fileName: String): Unit = {
    val fileNameWithoutExtension: String = removeExtensionFromFileName(fileName)
    val printWriter = new PrintWriter(new File("save/" + fileNameWithoutExtension + ".json"))
    printWriter.write(Json.prettyPrint(gameToJson(game)))
    printWriter.close()
  }

  def removeExtensionFromFileName(fileName: String): String = {
    if (fileName.contains(".")) {
      fileName.substring(0, fileName.lastIndexOf('.'))
    } else {
      fileName
    }
  }

  def gameToJson(game: GameInterface): JsObject = {
    Json.obj(
      "game" -> Json.obj(
        "players" -> game.players.map(p => p.toJson),
        "deck" -> game.deck.toJson,
        "trump" -> game.trump.toJson,
        "currentTurn" -> game.currentTurn.toJson,
        "active" -> game.active.nameToJson,
        /* "ok" -> game.ok.map(p => p.toJson) */
      )
    )
  }

  def createCard(c: JsValue): List[Card] = {
    val cardColor: String = (c \ "color").get.as[String]
    val cardValue: String = (c \ "value").get.as[String]
    val card: Card = CardStringConverter.parseCardStringToCardObject(cardColor + " " + cardValue)
    List(card)
  }
}
