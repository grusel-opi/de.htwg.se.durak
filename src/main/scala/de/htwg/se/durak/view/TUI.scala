package de.htwg.se.durak.view

import de.htwg.se.durak.Main.{newPlayer, playerList}
import de.htwg.se.durak.model.Player

object TUI {

  def parseInput(player: Player, input: String): Boolean = {
    val tokens = input.split(" ")
    tokens.size match {
      case 1 =>
        tokens(0) match {
          case "exit" => sys.exit(0)
          case _      => println("Could not parse input: " + tokens(0))
        }
      case 2 =>
        tokens(0) match {
          case "newPlayer" => playerList = newPlayer(tokens(1))
          case "card" =>
          case _      =>
        }
    }
    true
  }

}
