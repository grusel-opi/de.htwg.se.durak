package de.htwg.se.durak.model.fileIOComponent.fileIOXmlImpl

import java.io.{File, PrintWriter}

import com.google.inject.Guice
import de.htwg.se.durak.DurakModule
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.Card
import de.htwg.se.durak.model.fileIOComponent.FileIOInterface
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.{Game, Turn}
import de.htwg.se.durak.model.playerComponent.Player
import de.htwg.se.durak.util.cardConverter.CardStringConverter

import scala.xml.Node
import scala.xml.{Elem, PrettyPrinter}

class FileIO extends FileIOInterface {

  override def load(fileName: String): GameInterface = {
    println("XML LOAD")
    val fileNameWithoutExtension: String = removeExtensionFromFileName(fileName)
    val file: Elem = xml.XML.loadFile("save/" + fileNameWithoutExtension + ".xml")

    val players: List[Player] = createPlayersList(file)
    val deck: Deck = createDeck(file)
    val trump: Card = createTrump(file)
    val currentTurn: Turn = createTurn(file, players)
    val active: Player = createActivePlayer(file, players)
    val winners: List[Player] = createWinnersList(file)

    Game(players, deck, trump, currentTurn, active, winners)
  }

  override def save(game: GameInterface, fileName: String): Unit = saveString(game, fileName)

  def saveString(game: GameInterface, fileName: String): Unit = {
    val WIDTH: Int = 120
    val STEP: Int = 4
    val fileNameWithoutExtension: String = removeExtensionFromFileName(fileName)
    val printWriter: PrintWriter = new PrintWriter(new File("save/" + fileNameWithoutExtension + ".xml"))
    val prettyPrinter: PrettyPrinter = new PrettyPrinter(WIDTH, STEP)
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

  def createCard(c: Node): List[Card] = {
    val cardColor: String = (c \ "color").text.trim
    val cardValue: String = (c \ "value").text.trim
    val card: Card = CardStringConverter.parseCardStringToCardObject(cardColor + " " + cardValue)
    List(card)
  }

  def createPlayersList(file: Elem): List[Player] = {
    var players: List[Player] = Nil

    (file \\ "players" \\ "player").foreach(node => {
      val name = (node \ "name").text.toString.trim
      var handCards: List[Card] = Nil

      (node \\ "handCards" \ "card").foreach(card => {
        handCards = handCards ::: createCard(card)
      })

      players = players ::: List(Player(name, handCards))
    })

    players
  }

  def createDeck(file: Elem): Deck = {
    var cards: List[Card] = Nil

    (file \\ "deck" \ "card").foreach(card => {
      cards = cards ::: createCard(card)
    })

    Deck(cards)
  }

  def createTrump(file: Elem): Card = {
    createCard((file \\ "trump" \ "card").head).head
  }

  def createTurn(file: Elem, players: List[Player]): Turn = {
    val attackerName = (file \\ "attacker" \ "player" \ "name").text.toString.trim
    val victimName = (file \\ "victim" \ "player" \ "name").text.toString.trim
    val neighbourName = (file \\ "neighbour" \ "player" \ "name").text.toString.trim

    var attackCards: List[Card] = Nil
    var blockedAttackCards: List[Card] = Nil
    var blockingCards: List[Card] = Nil

    (file \\ "currentTurn" \ "attackCards" \ "card").foreach(c => {
      attackCards = attackCards ::: createCard(c)
    })

    (file \\ "blockedBy" \ "attackCards" \ "card").foreach(c => {
      blockedAttackCards = blockedAttackCards ::: createCard(c)
    })

    (file \\ "blockedBy" \ "blockingCards" \ "card").foreach(c => {
      blockingCards = blockingCards ::: createCard(c)
    })

    val attacker: Player = players.filter(p => p.name.equals(attackerName)).head
    val victim: Player = players.filter(p => p.name.equals(victimName)).head
    val neighbour: Player = players.filter(p => p.name.equals(neighbourName)).head
    var blockedBy: Map[Card, Card] = Map()

    for ((attackCard, blockingCard) <- blockedAttackCards zip blockingCards) {
      blockedBy = blockedBy + (attackCard -> blockingCard)
    }

    Turn(attacker, victim, neighbour, attackCards, blockedBy)
  }

  def createActivePlayer(file: Elem, players: List[Player]): Player = {
    val activePlayerName: String = (file \\ "active" \ "player" \ "name").text.toString.trim
    players.filter(p => p.name.equals(activePlayerName)).head
  }

  def createWinnersList(file: Elem): List[Player] = {
    var winners: List[Player] = Nil

    (file \\ "winners" \ "player").foreach(p => {
      val playerName: String = (p \ "name").text.toString.trim
      winners = winners ::: List(Player(playerName, Nil))
    })

    winners
  }

  def gameToXml(game: GameInterface): Elem = {
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
      <winners>
        {game.winners.map(p => p.nameToXml)}
      </winners>
    </game>
  }
}
