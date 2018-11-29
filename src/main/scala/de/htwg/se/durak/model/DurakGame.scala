package de.htwg.se.durak.model

import scala.util.Random

case class DurakGame(players: List[Player], deck: Deck, trump: Card, currentTurn: Turn, active: Player, ok: List[Player]) {
  def this(players: List[Player], deck: Deck)
  = this(players, deck.tail, deck.head, Turn(players.head, players.head, players.head, List[Card](), Map[Card, Card]())
    , active = players.head, ok = Nil)

  def this(players: List[Player]) = this(players, new Deck)

  def this() = this(new Player("default") :: Nil)

  def start(): DurakGame = {
    var newPlayers = List[Player]()
    var newCards = deck.popNCards(5)
    players.foreach(p => {
      newPlayers = p.pickCards(newCards._1) :: newPlayers
      newCards = newCards._2.popNCards(5)
    })
    val beginner = newPlayers(Random.nextInt() % players.size)
    val firstVictim = getNeighbor(beginner)
    val fistNeighbor = getNeighbor(firstVictim)
    val newTurn = Turn(beginner, firstVictim, fistNeighbor, Nil, Map[Card, Card]())
    copy(players = newPlayers, deck = newCards._2, currentTurn = newTurn, active = beginner, ok = Nil)
  }

  def addPlayer(player: Player): DurakGame = copy(players = player :: players)

  def win: DurakGame = copy(players = players.filterNot(p => p.equals(active)))

  def playOk: DurakGame = copy(ok = active :: ok, active = nextPlayersMove())

  def continue: DurakGame = copy(active = nextPlayersMove())

  def closeTurn(success: Boolean): Turn = { // TODO: dont forget to set new active!
    if (success) {
      new Turn(currentTurn.victim,
        currentTurn.neighbor,
        getNeighbor(currentTurn.neighbor))
    } else {
      new Turn(currentTurn.neighbor,
        getNeighbor(currentTurn.neighbor),
        getNeighbor(getNeighbor(currentTurn.neighbor)))
    }
  }

  def takeCards(): DurakGame = active match {
    case x if x.equals(currentTurn.victim) =>
      copy(active = active.pickCards(currentTurn.getAllCards), currentTurn = closeTurn(false))
    case _ => this // TODO: nonsense action; what do?
  }

  def playCard(card: Option[Card], cardToBlock: Option[Card]): (Boolean, DurakGame) = card match {
    case Some(c) =>
      if (active.hasCard(c)) {
        active match {
          case x if x.equals(currentTurn.victim) =>
            defend(c, cardToBlock)
          case y if y.equals(currentTurn.attacker)
            || y.equals(currentTurn.neighbor) =>
            attack(c)
        }
      } else (false, this) // TODO: player does not have card.. punish him!
    case None => (false, continue)
  }

  def defend(card: Card, cardToBlock: Option[Card]): (Boolean, DurakGame) = cardToBlock match {
    case Some(value) =>
      if (checkBlockCard(value, card)) {
        val newTurn = currentTurn.addBlockCard(value, card)
        val newActive = active.dropCards(card :: Nil)
        if (currentTurn.attackCards.isEmpty && ok.size > 1) {
          (true, copy(active = newActive, currentTurn = closeTurn(true)))
        } else {
          (true, copy(active = newActive, currentTurn = newTurn))
        }
      } else (false, this) // TODO: cannot use this card to defend => notify
    case None => (false, this) // TODO: must specify which card to block => exception?
  }

  def attack(card: Card): (Boolean, DurakGame) = if (checkAttackCard(card)) {
    (true, copy(active = active.dropCards(card :: Nil), ok = Nil, currentTurn = currentTurn.addAttackCard(card)))
  } else {
    (false, this)
  }

  def checkBlockCard(attack: Card, defend: Card): Boolean = defend.color match {
    case trump.color =>
      attack.color match {
        case trump.color => defend.value.compare(attack.value) > 0
        case _ => true
      }
    case _ =>
      attack.color match {
        case trump.color => false
        case _ => defend.value.compare(attack.value) > 0
      }
  }

  def checkAttackCard(card: Card): Boolean = if (currentTurn.attackCards.isEmpty) {
    true
  } else {
    val defended = currentTurn.blockedBy.keys.toList
    val defendedBy = currentTurn.blockedBy.values.toList
    val notDefended = currentTurn.attackCards
    (notDefended ::: defended ::: defendedBy).foreach(c => if (c.value.equals(card.value)) {
      return true
    })
    false
  }

  def nextPlayersMove(): Player = active match {
    case x if x.equals(currentTurn.neighbor)
      && !currentTurn.attacker.equals(currentTurn.neighbor) => currentTurn.attacker
    case _ => getNeighbor(active)
  }

  def getNeighbor(player: Player): Player = players.indexOf(player) match {
    case 0 => players.last
    case _ => players(players.indexOf(player) - 1)
  }
}
