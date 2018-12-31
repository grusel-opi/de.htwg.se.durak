package de.htwg.se.durak

import de.htwg.se.durak.view.Tui
import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.model.{Card, Deck, DurakGame, Player}
import de.htwg.se.durak.view.gui.SFXGui

import scala.io.StdIn._

object Durak {

  val controller = new Controller(new DurakGame())
  val tui = new Tui(controller)
  val gui: SFXGui.type = SFXGui

  def main(args: Array[String]): Unit = {

    gui.setController(controller)
    gui.main(args)


    println("Hello to durak")
    var input: String = ""
    do {
      println("Please enter a command (or help for help): ")
      input = readLine
      tui.processInputLine(input)
    } while (input != "q")
  }
}
