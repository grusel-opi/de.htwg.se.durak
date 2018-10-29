package de.htwg.se.durak.aview

import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.util.Observer


//object Tui {
//
//  val commands = Set("newPlayer", "card", "exit", "play")
//  val amountHandCards = 5
//  var players: List[Player] = List[Player]()
//  var deck = new Deck()
//
//  def parseInput(input: String): Boolean = {
//    val tokens = input.split(" ")
//    tokens.size match {
//      case 1 =>
//        tokens(0) match {
//          case "exit" => sys.exit(0)
//          case "play" =>
//          case "menu" => {
//            println("Welcome to Durak!");
//            print("Please enter the number of players: ")
//
//            val numberOfPlayers: Int = scala.io.StdIn.readLine.toInt
//
//            numberOfPlayers match {
//              case x if x <= 1 => {
//                println("Please enter a number greater than 1!")
//                println()
//                parseInput("menu")
//              }
//              case _ => {
//                println("number of players set to: " + numberOfPlayers)
//                println()
//
//                for (i <- 0 to numberOfPlayers - 1) {
//                  println("Enter name for Player" + (i + 1) + ":")
//                  val playerName: String = scala.io.StdIn.readLine.toString
//
//                }
//
//              }
//            }
//
//            println("FINITO!")
//          }
//          case _ => println("Could not parse input: " + tokens(0))
//        }
//      case 2 =>
//        tokens(0) match {
//          case "newPlayer" =>
//          case "card" =>
//          case _ =>
//        }
//    }
//    true
//  }
//
//  //  def createPlayers(numberOfPlayers: Int): List[Player] = {
//  //
//  //  }
//
//  def mainMenu(): Unit = {
//    this.parseInput("menu");
//  }
//
//  def newGame(players: List[Player]): DurakGame = new DurakGame(players)
//
//  def newPlayer(name: String): Unit = {
//    val newDeck = deck.popNCards(amountHandCards)
//    deck = newDeck._2
//    players = Player(name, newDeck._1) :: players
//  }
//
//
//}

class Tui(controller: Controller) extends Observer {

  controller.add(this)

  def processInputLine(input: String): Unit = {
    input match {
      case "q" => println("quit")
      case "h" => println("help")
      case "n" => {
        println("Welcome to Durak!")
        print("Please enter the number of players: ")

        val numberOfPlayers: Int = readInt
        var playerNames: List[String] = Nil

        for (i <- 0 to numberOfPlayers - 1) {
          print("Please enter name for Player" + (i + 1) + ": ")

          val playerName: String = readLine()
          playerNames = playerName :: playerNames
        }
        println()

        controller.createNewGame(numberOfPlayers, playerNames)
      }
      case _ => println("unknown command: " + input + "!")
    }
  }

  override def update: Unit = println(controller.durakGameToString)
}