package de.htwg.se.durak.model

case class DurakGame(players: List[Player], deck: Deck, trump: Card) {
  def this(players: List[Player], deck: Deck) = this(players, deck.tail, deck.head)
  def this(players: List[Player]) = this(players, new Deck)

  def attack(attacker: Player, victim: Player, attackCards: Set[Card]): Turn = {
    val neighbors = getNeighbors(victim)
    new Turn(victim, attacker, neighbors._1, neighbors._2, attackCards)
  }

  private def getNeighbors(player: Player) : (Player, Player) = {
    val size = players.length
      players.indexOf(player) match {
      case x if x > 0 && x < players.length -1 => (players(players.indexOf(player) - 1), players(players.indexOf(player) + 1))
      case x if x == size - 1                  => (players(players.length -2), players.head)
      case 0                                   => (players(1), players.last)
    }

  }

  override def toString: String = {
    var s: String = ""

    players.foreach(p => s = s + p + "\n")

    s = s + "\nTRUMP card: " + trump

    s
  }
}
