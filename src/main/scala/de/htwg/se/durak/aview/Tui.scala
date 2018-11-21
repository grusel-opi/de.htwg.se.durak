package de.htwg.se.durak.aview

import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.model.{Card, CardColor, CardValue}
import de.htwg.se.durak.util.{CardStringConverter, Observer}

class Tui(controller: Controller) extends Observer {

  controller.add(this)
  val converter = CardStringConverter

  def processInputLine(input: String): Unit = {
    val tokens = input.split(" ")
    tokens.head match {
      case "help"|"h" => printHelp()
      case "new"    => controller.newGame()
      case "player" => controller.newPlayer(tokens.last)
      case "play"   => controller.playCard(parseCards(tokens.tail.toList)._1, parseCards(tokens.tail.toList)._2)
      case "take"   => controller.takeCards()
      case "ok"     => controller.playOK()
      case "q"      => System.exit(0)
      case "players" => println(controller.players.mkString(" "))
      case _        => println("Bitte was?")
    }
  }

  def printHelp(): Unit = {
    println()
    println("Hello, this is help text!")
    println("Fist add players, then start game!")
    println("type [player <name>] for new player")
    println("type [new] for new game")
    println("type [play <AttackCard>] or [play <DefendCard> <CardToDefend>] for attack / defense")
    println("type [play] for check (do nothing)")
    println("type [ok] for indicating you are done with the current turn")
    println("type [take] to take all cards if you are the victim of the current turn")
  }

  def parseCards(input: List[String]): (Option[Card], Option[Card]) = {
    input.size match {
      case 0 => (None, None)
      case 2 => (Some(Card(converter.parseColorString(input.head), converter.parseValueString(input.last))), None)
      case 4 => (Some(Card(converter.parseColorString(input.head), converter.parseValueString(input(1)))),
          Some(Card(converter.parseColorString(input(2)), converter.parseValueString(input(3)))))
      case _ => (None, None)// TODO: cannot play more than two cards at once; exception?
    }

  }

  override def update(): Unit = {
    println("Current Turn:")
    println(controller.game.currentTurn.toString)
    println("players turn: " + controller.game.active.toString)
    print("cards: \n")
    controller.game.active.handCards.foreach(c => print(" " + converter.parseCardObject(c)+ ", "))
    println()
  }
}