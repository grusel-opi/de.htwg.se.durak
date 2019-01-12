package de.htwg.se.durak.aview

import de.htwg.se.durak.controller.controllerComponent.ControllerInterface
import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.util.cardConverter.CardStringConverter

import scala.swing.Reactor
import scala.util.{Failure, Success, Try}

class Tui(controller: ControllerInterface) extends Reactor {

  listenTo(controller)

  private val converter = CardStringConverter

  def processInputLine(input: String): Unit = {

    val tokens = input.split(" ")

    tokens.head match {

      case "play" =>
        parseCards(tokens.tail.toList) match {
          case Success(cards) => controller.playCard(cards._1, cards._2)
          case Failure(ex)    => System.err.println("Error while parsing cards: " + ex.getMessage)
        }

      case "player" | "p" =>
        if (tokens.size > 1) {
          controller.newPlayer(tokens.tail.mkString(" "))
        }
      case "undo"       => controller.undo()
      case "redo"       => controller.redo()
      case "help" | "h" => printHelp()
      case "new"        => controller.newGame()
      case "take"       => controller.takeCards()
      case "ok"         => controller.playOk()
      case "q" | "exit" => System.exit(0)
      case "players"    => println(controller.players.mkString(" "))
      case _            => println("Bitte was?")
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

  def parseCards(input: List[String]): Try[(Option[Card], Option[Card])] = {
    input.size match {
      case 0 => Try(None,None)
      case 2 => Try(Some(Card(converter.parseColorString(input.head), converter.parseValueString(input.last))), None)
      case 4 => Try((Some(Card(converter.parseColorString(input.head), converter.parseValueString(input(1)))),
                     Some(Card(converter.parseColorString(input(2)), converter.parseValueString(input(3))))))
      case _ => Try(None, None) // TODO: cannot use more than two cards at once; exception?
    }

  }

  reactions += {
    case _ => updateTui()
  }

  def updateTui(): Unit = {
//    println()
//    println("=============================================")
//    println("players turn: " + controller.game.active.toString)
//    println("Trump: [" + controller.game.trump.color + "] " + controller.game.trump.value)
//    println(controller.game.currentTurn.toString)
//    println("players who are \"ok\": " + controller.game.ok.mkString(", "))
//    print("cards: ")
//    print(controller.game.active.handCards.mkString(", ") + "\n")
//    println("=============================================")
//    println()

    val trumpCardColorValueArray: Array[String] = controller.trumpCardToString().split(" ")
    val trumpCardColorAsString: String = trumpCardColorValueArray(0)
    val trumpCardValueAsString: String = trumpCardColorValueArray(1)

    println()
    println("=============================================")
    println("players turn: " + controller.activePlayerHandCardsToString())
    println("Trump: [" + trumpCardColorAsString + "] " + trumpCardValueAsString)
    println(controller.currentTurnToString())
    println("players who are \"ok\": " + controller.playersOkToString())
    print("cards: ")
    print(controller.activePlayerHandCardsToString() + "\n")
    println("=============================================")
    println()
  }
}