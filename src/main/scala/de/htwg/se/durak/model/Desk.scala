package de.htwg.se.durak.model

case class Desk(players: List[Player], deck: Deck, trump: Card, currentTurn: Option[Turn]) {
  def this(players: List[Player], deck: Deck) = this(players, deck, deck.getLastCard, None)
  def this(players: List[Player]) = this(players, new Deck().shuffle)

  def attack(attacker: Player, victim: Player, attackCards: List[Card]): Desk = {
    val neighbors: (Player, Player) = getNeighbors(victim)
    val newTurn: Turn = Turn(attacker, victim, neighbors._1, neighbors._2, attackCards, currentTurn.get.blockCards, trump)
    Desk(players, deck, trump, Some(newTurn))
  }

  def getNeighbors(player: Player): (Player, Player) = players.indexOf(player) match {
    case x if x > 0 && x < players.length - 1 => (players(players.indexOf(player) - 1), players(players.indexOf(player) + 1))
    case x if x == players.length - 1 => (players(players.length - 2), players.head)
    case 0 => (players(1), players.last)
  }
}
