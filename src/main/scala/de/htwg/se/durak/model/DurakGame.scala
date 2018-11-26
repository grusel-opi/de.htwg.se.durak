package de.htwg.se.durak.model

import scala.util.Random

case class DurakGame(var players: List[Player],var deck: Deck, trump: Card, var currentTurn: Turn) {
  def this(players: List[Player], deck: Deck)
  = this(players, deck, deck.lastCard, Turn(players.head, players.head, players.head, List[Card](), Map[Card, Card]()))
  def this(players: List[Player]) = this(players, new Deck)
  def this() = this(new Player("default")::Nil)

  var active: Player = players.head
  var ok: List[Player] = Nil

  def addPlayer(player: Player): DurakGame = copy(player::players)

  def win(): DurakGame = copy(players.filterNot(p => p.equals(active)))

  def initHandCards(): Unit = {
    var newPlayers: List[Player] = Nil
    players.foreach(p => {
      val pop = deck.popNCards(5)
      deck = pop._2
      newPlayers = p.pickCards(pop._1)::newPlayers // TODO: players need to be replaced after pick up cards
    })
    players = newPlayers
  }

  def closeTurn(success: Boolean): Unit = {
    if (success) {
      currentTurn = new Turn(currentTurn.victim, currentTurn.neighbor, getNeighbor(currentTurn.neighbor))
    } else {
      currentTurn = new Turn(currentTurn.neighbor, getNeighbor(currentTurn.neighbor), getNeighbor(getNeighbor(currentTurn.neighbor)))
    }
  }

  def takeCards(): Unit = active match {
    case x if x.equals(currentTurn.victim) => {
      active = active.pickCards(currentTurn.attackCards)
      active = active.pickCards(currentTurn.blockedBy.values.toList)
      active = active.pickCards(currentTurn.blockedBy.keys.toList)
      closeTurn(false)
    }
    case _ => // TODO: nonsense action; what do?
  }

  def playCard(card: Option[Card], cardToBlock: Option[Card]): Unit = {
    card match {
      case Some(c) =>
      if (active.hasCard(c)) {
        active match {
          case x if x.equals(currentTurn.victim) =>
            defend(c, cardToBlock)
          case y if y.equals(currentTurn.attacker)
            || y.equals(currentTurn.neighbor) =>
            attack(c)
        }
      } else {
      }
      case None => nextMove()
    }
  }

  def defend(card: Card, cardToBlock: Option[Card]): Boolean = {
    cardToBlock match {
      case Some(value) =>
        if (checkBlockCard(value, card)) {
          currentTurn = currentTurn.addBlockCard(value, card)
          active = active.dropCards(card::Nil)
          if (currentTurn.attackCards.isEmpty
            && (ok.contains(currentTurn.attacker)
            &&  ok.contains(currentTurn.neighbor))) {
            closeTurn(true)
          }
          true
        } else { // TODO: notify
          false
        }
      case None => {
       false // TODO: exception?
      }
    }
  }

  def attack(card: Card): Boolean = {
    if (checkAttackCard(card)) {
      currentTurn = currentTurn.addAttackCard(card)
      active.dropCards(card::Nil)
      ok = Nil
      true
    } else {
      false
    }
  }

  def start(): Unit = {
    initHandCards()
    val beginner = players(math.abs(Random.nextInt()) % players.size)
    active = beginner
    currentTurn = Turn(beginner, getNeighbor(beginner), getNeighbor(getNeighbor(beginner)), Nil, Map[Card, Card]())
  }

  def checkBlockCard(attack: Card, defend: Card): Boolean = {
    defend.color match {
      case trump.color =>
        attack.color match {
          case  trump.color => defend.value.compare(attack.value) > 0
          case  _           => true
        }
      case _ =>
        attack.color match {
          case trump.color => false
          case _           => defend.value.compare(attack.value) > 0
        }
    }
  }

  def checkAttackCard(card: Card): Boolean = {
    var res = false
    val defended = currentTurn.blockedBy.keys.toList
    val defendedBy = currentTurn.blockedBy.values.toList
    (currentTurn.attackCards:::defended:::defendedBy).foreach(c => if (c.value.equals(card.value)) res = true)
    res
  }

  def playOk(): Unit = {
    ok = active::ok
    nextMove()
  }

  def nextMove(): Unit = active match {
    case x if x.equals(currentTurn.neighbor) => active = currentTurn.attacker
    case _ => active = getNeighbor(active)
  }

  def getNeighbor(player: Player) : Player = players.indexOf(player) match {
    case 0 => players.last
    case _ => players(players.indexOf(player) - 1)
  }
}
