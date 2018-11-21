package de.htwg.se.durak

import de.htwg.se.durak.aview.Tui
import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.model.{Deck, DurakGame}
import de.htwg.se.durak.util.CardStringConverter

import scala.io.StdIn._

object Durak {

  val controller = new Controller(new DurakGame())
  val tui = new Tui(controller)
  controller.notifyObservers()

  def main(args: Array[String]): Unit = {
    var input: String = ""
    do {
      print("Please enter a command: ")
      input = readLine
      tui.processInputLine(input)
    } while (input != "q")
  }
}
