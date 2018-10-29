package de.htwg.se.durak.view

import de.htwg.se.durak.model.{Deck, DurakGame, Player}

object TUI {

  val commands = Set("newPlayer", "card", "exit", "play")
  val amountHandCards = 5
  var players: List[Player] = List[Player]()
  var deck = new Deck()

  def parseInput(player: Player, input: String): Boolean = {
    val tokens = input.split(" ")
    tokens.size match {
      case 1 =>
        tokens(0) match {
          case "exit" => sys.exit(0)
          case "play" =>
          case _      => println("Could not parse input: " + tokens(0))
        }
      case 2 =>
        tokens(0) match {
          case "newPlayer" =>
          case "card" =>
          case _      =>
        }
    }
    true
  }

  def newGame(players: List[Player]): DurakGame = new DurakGame(players)

  def newPlayer(name: String): Unit = {
    val newDeck = deck.popNCards(amountHandCards)
    deck = newDeck._2
    players = Player(name, newDeck._1)::players
  }


}
