package de.htwg.se.durak

import de.htwg.se.durak.view.TUI

import scala.io.StdIn._

object Main {
  def main(args: Array[String]): Unit = {
    val tui = TUI
    var input = readLine()
    while (!input.equals("exit")) {
      var res = tui.parseInput(input)
      input = readLine()
    }
  }
}
