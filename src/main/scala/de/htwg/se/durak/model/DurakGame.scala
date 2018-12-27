package de.htwg.se.durak.model

import scala.util.Random

case class DurakGame(players: List[Player], deck: Deck, trump: Card, currentTurn: Turn, active: Player, ok: List[Player]) {

  def this(players: List[Player], deck: Deck) = this(players, deck, deck.cards.last,
    new Turn(players.head, players.head, players.head), players.head, Nil)

  def this(players: List[Player]) = this(players, new Deck().shuffle)

  def this() = this(List(new Player("default")))

  def start(): DurakGame = {
    var cardsDeckTuple: (List[Card], Deck) = deck.popNCards(5)
    for (i <- players.indices) {
      players(i).pickCards(cardsDeckTuple._1)
        if (i < players.size - 1) {
          cardsDeckTuple = cardsDeckTuple._2.popNCards(5)
        }
    }
    val beginner: Player = players(math.abs(Random.nextInt()) % players.size)
    val firstVictim: Player = getNeighbor(beginner)
    val fistNeighbor: Player = getNeighbor(firstVictim)
    val newTurn = Turn(beginner, firstVictim, fistNeighbor, Nil, Map[Card, Card]())

    copy(deck = cardsDeckTuple._2, currentTurn = newTurn, active = beginner, ok = Nil)
  }

  def win: DurakGame = copy(players = players.filterNot(p => p.equals(active)))

  def playOk: DurakGame = {
    if (active.ne(currentTurn.victim) && currentTurn.attackCards.isEmpty){
      if (ok.nonEmpty) {
        val (nextTurn, newDeck) = closeTurn(true)
        copy(ok = Nil, currentTurn = nextTurn, active = nextTurn.attacker, deck = newDeck)
      } else {
        copy(ok = active :: ok, active = nextPlayersMove())
      }
    } else {
      this
    }
  }

  def continue: DurakGame = copy(active = nextPlayersMove())

  def closeTurn(success: Boolean): (Turn, Deck) = { // TODO: dont forget to set new active on every usage!
    var tmpDeck = (List[Card](), deck)
    currentTurn.getPlayers.foreach(p => {
      val missingAmount = 5 - p.handCards.size
      if (missingAmount > 0) {
        tmpDeck = tmpDeck._2.popNCards(missingAmount)
        p.pickCards(tmpDeck._1)
      }
    })
    if (success) {
      (new Turn(currentTurn.victim,
        currentTurn.neighbor,
        getNeighbor(currentTurn.neighbor)),
        tmpDeck._2)
    } else {
      (new Turn(currentTurn.neighbor,
        getNeighbor(currentTurn.neighbor),
        getNeighbor(getNeighbor(currentTurn.neighbor))),
        tmpDeck._2)
    }
  }

  def takeCards(): DurakGame = active match {
    case x if x.equals(currentTurn.victim) =>
      active.pickCards(currentTurn.getCards)
      val (nextTurn, newDeck) = closeTurn(false)
      copy(currentTurn = nextTurn, active = currentTurn.neighbor, deck = newDeck)
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
      if (checkBlockCard(value, card)) { // TODO: ordering of params for checking intuitive?
        val newTurn = currentTurn.addBlockCard(value, card)
        active.dropCards(card :: Nil)
        if (newTurn.attackCards.isEmpty) {
          (true, copy(currentTurn = newTurn, active = nextPlayersMove(), ok = Nil))
        } else {
          (true, copy(currentTurn = newTurn, ok = Nil))
        }
      } else (false, this) // TODO: cannot use this card to defend => notify
    case None => (false, this) // TODO: must specify which card to block => exception?
  }

  def attack(card: Card): (Boolean, DurakGame) = if (checkAttackCard(card)) {
    active.dropCards(card :: Nil)
    (true, copy(ok = Nil, currentTurn = currentTurn.addAttackCard(card)))
  } else {
    (false, this)
  }

  // true if second card is greater
  def checkBlockCard(against: Card, use: Card): Boolean = {
    use.color match {
      case trump.color =>
        against.color match {
          case trump.color => use.value.compare(against.value) > 0
          case _ => true
        }
      case _ =>
        against.color match {
          case trump.color => false
          case _ => use.value.compare(against.value) > 0
        }
    }
  }

//  // true if first card is greater
//  def compareCards(first: Card, second: Card): Int = {
//    first.color match {
//      case trump.color =>
//        second.color match {
//          case trump.color => first.value.compare(second.value)
//          case _           => 1
//        }
//      case _ =>
//        second.color match {
//          case trump.color => -1
//          case _           => first.value.compare(second.value)
//        }
//    }
//  }

  def checkAttackCard(card: Card): Boolean = if (currentTurn.attackCards.isEmpty) {
    true
  } else {
    val defended = currentTurn.blockedBy.keys.toList
    val defendedBy = currentTurn.blockedBy.values.toList
    val notDefended = currentTurn.attackCards
    (notDefended ::: defended ::: defendedBy).foreach(c =>
      if (c.value.equals(card.value)) {
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

  def computePossibilities(): Either[List[Card], Map[Card, Card]] = {
    if (active.equals(currentTurn.attacker) || active.equals(currentTurn.neighbor)) {
      Left(computeAttackerPossibilities())
    } else {
      Right(computeDefenderPossibilities())
    }
  }

  def computeAttackerPossibilities(): List[Card] = {
    if (currentTurn.attackCards.isEmpty) {
      active.handCards
    } else {
      active.handCards.filter(c => checkAttackCard(c))
    }
  }

  def computeDefenderPossibilities(): Map[Card, Card] = {

  }

}
