package de.htwg.se.durak.aview

import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.model.{Card, CardColor, CardValue}
import de.htwg.se.durak.util.Observer

class Tui(controller: Controller) extends Observer {

  controller.add(this)

  def processInputLine(input: String): Unit = {
    val tokens = input.split(" ")
    tokens.head match {
      case "help"   =>
      case "new"    => controller.newGame()
      case "player" => controller.newPlayer(tokens.last)
      case "play"   => controller.playCard(parseCards(tokens.tail.toList)._1, parseCards(tokens.tail.toList)._2)
      case "take"   => controller.takeCards()
      case "ok"     => controller.playOK()
    }
  }

  def printHelp(): Unit = {
    println("fist add players, then start game!")
    println("type [player <name>] for new player")
    println("type [new] for new game")
    println("type [play <AttackCard>] or [play <DefendCard> <CardToDefend>] for attack / defense")
    println("type [play] for check (do nothing)")
    println("type [ok] for indicating you are done with the current turn")
    println("type [take] to take all cards if you are the victim of the current turn")
  }

  def parseCards(input: List[String]): (Option[Card], Option[Card]) = {
    input.size match {
      case 0 =>
      case 2 =>
      case 4 =>
      case _ =>
    }

  }

  def printCard(card: Card): String = {
    // TODO: use cardConverter
    "card"
  }

  override def update(): Unit = {
    println()
  }
}