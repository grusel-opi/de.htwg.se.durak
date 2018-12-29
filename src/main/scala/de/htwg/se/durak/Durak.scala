package de.htwg.se.durak

import de.htwg.se.durak.view.Tui
import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.model.{Card, Deck, DurakGame, Player}

import scala.io.StdIn._

object Durak {

  val controller = new Controller(new DurakGame())
  val tui = new Tui(controller)
  //controller.notifyObservers() //TODO: Why?

  def main(args: Array[String]): Unit = {
    println("Hello to durak")
    var input: String = ""
    do {
      println("Please enter a command (or help for help): ")
      input = readLine
      tui.processInputLine(input)
    } while (input != "q")
  }
}
