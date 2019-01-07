package de.htwg.se.durak.model

import de.htwg.se.durak.model.exceptions.{IllegalTurnException, MissingBlockingCardException, VictimHasNotEnoughCardsToBlockException}

import scala.util.Random

case class DurakGame(players: List[Player], deck: Deck, trump: Card, currentTurn: Turn, active: Player, ok: List[Player],
                     winner: Option[Player]) {

  def this(players: List[Player], deck: Deck) = this(players, deck, deck.cards.last,
    new Turn(players.head, players.head, players.head), players.head, Nil, None)

  def this(players: List[Player]) = this(players, new Deck().shuffle)

  def this() = this(List(new Player("default")))

  implicit val CardOrdering: Ordering[Card] = (x: Card, y: Card) => {
    if (x.color.equals(y.color)) {
      x.value.compare(y.value)
    } else if (x.color.equals(trump.color)) {
      1
    } else if (y.color.equals(trump.color)) {
      -1
    } else {
      0
    }
  }

  def start(): DurakGame = {
    var cardsDeckTuple: (List[Card], Deck) = deck.popNCards(5)
    for (i <- players.indices) {
      players(i).pickCards(cardsDeckTuple._1)
      players(i).sortHandCards
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

  def playOk: DurakGame = {
    if (active.equals(currentTurn.attacker) && currentTurn.attackCards.isEmpty) {
      if (ok.nonEmpty) {
        println("1")
        val (nextTurn, newDeck) = closeTurn(true)
        copy(ok = Nil, currentTurn = nextTurn, active = nextTurn.attacker, deck = newDeck)
      } else {
        if (players.size > 2) {
          if (currentTurn.attackCards.nonEmpty) {
            println("2")
            copy(ok = active :: ok, active = nextPlayersMove())
          } else if (!currentTurn.blockedBy.isEmpty) {
            println("3")
            val (nextTurn, newDeck) = closeTurn(true)
            copy(ok = Nil, currentTurn = nextTurn, active = nextTurn.attacker, deck = newDeck)
          } else {
            println("4")
            this
          }
        } else {
          if (!currentTurn.attackCards.isEmpty) {
            println("5")
            copy(ok = active :: ok, active = nextPlayersMove())
          } else if (!currentTurn.blockedBy.isEmpty) {
            println("6")
            val (nextTurn, newDeck) = closeTurn(true)
            copy(ok = Nil, currentTurn = nextTurn, active = nextTurn.attacker, deck = newDeck)
          } else {
            println("7")
            this
          }
        }
      }
    } else {
      println("8")
      continue
    }
  }

  def continue: DurakGame = copy(active = nextPlayersMove())

  def closeTurn(success: Boolean): (Turn, Deck) = { //dont forget to set new active on every usage!
    var tmpDeck = (List[Card](), deck)
    currentTurn.getPlayers.foreach(p => {
      val missingAmount = 5 - p.handCards.size
      if (missingAmount > 0) {
        tmpDeck = tmpDeck._2.popNCards(missingAmount)
        p.pickCards(tmpDeck._1)
        p.sortHandCards
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
      active.sortHandCards
      val (nextTurn, newDeck) = closeTurn(false)
      copy(currentTurn = nextTurn, active = currentTurn.neighbor, deck = newDeck)
    case _ => this // nonsense action; ignored in tui, blocked in gui
  }

  def playCard(card: Option[Card], cardToBlock: Option[Card]): DurakGame = card match {
    case Some(c) =>
      if (active.hasCard(c)) {
        active match {
          case x if x.equals(currentTurn.victim) =>
            defend(c, cardToBlock)
          case y if y.equals(currentTurn.attacker)
            || y.equals(currentTurn.neighbor) =>
            attack(c)
        }
      } else this // player does not have card.. punish him!
    case None => continue
  }

  def defend(card: Card, cardToBlock: Option[Card]): DurakGame = cardToBlock match {
    case Some(enemy) =>
      if (checkBlockCard(card, enemy)) {
        if (currentTurn.attackCards.size <= currentTurn.victim.handCards.size) {
          val newTurn = currentTurn.addBlockCard(enemy, card)
          active.dropCards(card :: Nil)
          if (newTurn.attackCards.isEmpty) {
            if (!active.handCards.isEmpty) {
              copy(currentTurn = newTurn, active = nextPlayersMove(), ok = Nil)
            } else {
              println("YO!")
              this
            }
          } else {
            copy(currentTurn = newTurn, ok = Nil)
          }
        } else {
          throw new VictimHasNotEnoughCardsToBlockException()
        }
      } else {
        throw new IllegalTurnException()
      }
    case None => throw new MissingBlockingCardException()
  }

  def attack(card: Card): DurakGame = if (checkAttackCard(card)) {
    active.dropCards(card :: Nil)
    copy(ok = Nil, currentTurn = currentTurn.addAttackCard(card))
  } else {
    this
  }

  def shove(card: Card): DurakGame = {
    throw new Exception("Not implemented yet :(")
  }

  def checkBlockCard(use: Card, against: Card): Boolean = {
    if (use.color.equals(against.color)) {
      use.value.compare(against.value) > 0
    } else if (use.color.equals(trump.color)) {
      true
    } else {
      false
    }
  }

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
      Right(computeDefenderPossibilities(None))
    }
  }

  def computeAttackerPossibilities(): List[Card] = {
    if (currentTurn.attackCards.isEmpty) {
      active.handCards
    } else {
      active.handCards.filter(c => checkAttackCard(c)).sorted
    }
  }

  def computeDefenderPossibilities(player: Option[Player]): Map[Card, Card] = {
    val cardsToBlock = currentTurn.attackCards.sorted
    val availableCards = (player match {
      case Some(p) => p.handCards
      case None => active.handCards
    }).sorted.reverse
    var result: Map[Card, Card] = Map[Card, Card]()
    cardsToBlock.foreach(c => {
      availableCards.foreach(a => {
        if (checkBlockCard(a, c)) {
          result = result ++ Map(c -> a)
        }
      })
    })
    result
  }

  def checkIfGameIsOver(): Boolean = {
    if (players.size == 1) {
      true
    } else {
      false
    }
  }

  def checkIfPlayerHasWon(): DurakGame = {
    println("checkIfPlayerHasWon...")
    println("active hand card size: " + active.handCards.size)
    if (active.handCards.isEmpty) {
      if (winner == None) {
        if (players.size == 2) {
          println("1")
          copy(players = players.filterNot(p => p.equals(active)), winner = Some(active))
        } else {
          if (active.equals(currentTurn.attacker) || active.equals(currentTurn.neighbor)) {
            println("2")
            val newTurn = new Turn(currentTurn.neighbor, currentTurn.victim, currentTurn.neighbor, currentTurn.attackCards,
              blockedBy = currentTurn.blockedBy)
            copy(players = players.filterNot(p => p.equals(active)), currentTurn = newTurn, active = nextPlayersMove(),
              winner = Some(active))
          } else {
            println("3")
            val newTurn = new Turn(currentTurn.neighbor, currentTurn.attacker, currentTurn.neighbor)
            val winner = active
            copy(players = players.filterNot(p => p.equals(active)), currentTurn = newTurn, active = nextPlayersMove(),
              winner = Some(active))
          }
        }
      } else {
        println("4")
        if (players.size == 2) {
          copy(players = players.filterNot(p => p.equals(active)))
        } else {
          println("5")
          copy(players = players.filterNot(p => p.equals(active)), active = nextPlayersMove())
        }
      }
    } else {
      println("6")
      this
    }
  }
}
