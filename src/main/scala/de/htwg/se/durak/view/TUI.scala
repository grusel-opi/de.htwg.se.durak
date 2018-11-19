package de.htwg.se.durak.view

import de.htwg.se.durak.model.{Deck, Desk, Player}

object TUI {

  val commands = Set("newPlayer", "card", "exit", "play")
  val amountHandCards = 5
  var players: List[Player] = List[Player]()
  var game: Desk = new Desk(Nil)
  var deck: Deck = new Deck()

  def parseInput(input: String): Boolean = {
    val tokens = input.split(" ")
    tokens.size match {
      case 1 =>
        tokens(0) match {
          case "play" => {
            if (players.size < 2) {
              println("Please add players first!")
            } else {
              newGame(players)
            }
          }
          case _      => println("Could not parse input: " + tokens(0))
        }
      case 2 =>
        tokens(0) match {
          case "newPlayer" => newPlayer(tokens(1))
          case "card" => {
            if (game.players.size < 2) {
              println("Please add players and then type play")
            }
          }
          case _      =>
        }
    }
    true
  }

  def newGame(players: List[Player]): Unit = game = new Desk(players)

  def newPlayer(name: String): Unit = {
    val newDeck = deck.popNCards(amountHandCards)
    deck = newDeck._2
    players = Player(name, newDeck._1.toList)::players
  }


}
