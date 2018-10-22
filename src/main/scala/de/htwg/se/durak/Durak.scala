package de.htwg.se.durak

import de.htwg.se.durak.model._

import scala.io.StdIn._

object Durak {
  val commands = Set("newPlayer", "card", "exit")
  val shuffledDeck: Deck = new Deck().shuffle
  var playerList: List[Player] = List()

  def parseInput(player: Player, input: String): Boolean = {
    val tokens = input.split(" ")
    tokens.size match {
      case 1 => {
        tokens(0) match {
          case "exit" => sys.exit(0)
          case _      => {
            println("Could not parse input: " + tokens(0))
          }
        }
      }
      case 2 => {
        tokens(0) match => {
          case "newPlayer" => playerList = newPlayer(tokens(1))
          case "card" =>
          case _      =>
        }
      }
    }
    true
  }

  def newPlayer(name: String) = Player(name, None, None)::playerList

  def main(args: Array[String]): Unit = {
    val c = Card(CardColor.Clubs, CardValue.Ace)
    val d = Card(CardColor.Clubs, CardValue.Eight)
  }
}
