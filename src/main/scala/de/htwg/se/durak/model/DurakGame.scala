package de.htwg.se.durak.model

import de.htwg.se.durak.Main.playerList

case class DurakGame(players: List[Player], deck: Deck, trump: Card) {

  def this(players: List[Player], deck: Deck) = this(players, deck.tail, deck.head)

//  def newPlayer(name: String) = Player(name, None, None)::playerList


    def this(players: List[Player]) = this(players, new Deck)


  def getNeighbors(player: Player) : (Player, Player) = {
    val size = players.length
      players.indexOf(player) match {
      case x if x > 0 && x < players.length -1 => (players(players.indexOf(player) - 1), players(players.indexOf(player) + 1))
      case x if x == size - 1                  => (players(players.length -2), players.head)
      case 0                                   => (players(1), players.last)
    }

  }


}
