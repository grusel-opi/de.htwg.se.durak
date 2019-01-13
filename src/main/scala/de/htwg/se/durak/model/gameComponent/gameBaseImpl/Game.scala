package de.htwg.se.durak.model.gameComponent.gameBaseImpl

import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.model.playerComponent.Player
import de.htwg.se.durak.util.customExceptions.{IllegalTurnException, MissingBlockingCardException,
  NoCardsToTakeException, VictimHasNotEnoughCardsToBlockException}

import scala.util.Random

case class Game(players: List[Player], deck: Deck, trump: Card, currentTurn: Turn, active: Player, ok: List[Player],
                winner: Option[Player]) extends GameInterface {

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

  val NUMBER_OF_HAND_CARDS = 5

  def start(): Game = {
    var cardsDeckTuple: (List[Card], Deck) = deck.popNCards(NUMBER_OF_HAND_CARDS)
    for (i <- players.indices) {
      players(i).pickCards(cardsDeckTuple._1)
      players(i).sortHandCards
      if (i < players.size - 1) {
        cardsDeckTuple = cardsDeckTuple._2.popNCards(NUMBER_OF_HAND_CARDS)
      }
    }
    val beginner: Player = players(math.abs(Random.nextInt()) % players.size)
    val firstVictim: Player = getNeighbour(beginner)
    val fistNeighbor: Player = getNeighbour(firstVictim)
    val newTurn = Turn(beginner, firstVictim, fistNeighbor, Nil, Map[Card, Card]())

    copy(deck = cardsDeckTuple._2, currentTurn = newTurn, active = beginner, ok = Nil)
  }

  def playOk(): Game = {
    if (active.equals(currentTurn.attacker) && currentTurn.attackCards.isEmpty) {
      if (ok.nonEmpty) {
        val (nextTurn, newDeck) = closeTurn(true)
        println("1")
        copy(ok = Nil, currentTurn = nextTurn, active = nextTurn.attacker, deck = newDeck)
      } else {
        if (players.size > 2) {
          if (currentTurn.attackCards.nonEmpty) {
            println("2")
            copy(ok = active :: ok, active = nextPlayersMove())
          } else if (currentTurn.blockedBy.nonEmpty) {
            val (nextTurn, newDeck) = closeTurn(true)
            println("3")
            copy(ok = Nil, currentTurn = nextTurn, active = nextTurn.attacker, deck = newDeck)
          } else {
            this
          }
        } else {
          if (currentTurn.attackCards.nonEmpty) {
            copy(ok = active :: ok, active = nextPlayersMove())
          } else if (currentTurn.blockedBy.nonEmpty) {
            val (nextTurn, newDeck) = closeTurn(true)
            println("4")
            copy(ok = Nil, currentTurn = nextTurn, active = nextTurn.attacker, deck = newDeck)
          } else {
            println("5")
            this
          }
        }
      }
    } else {
      println("6")
      continue()
    }
  }

  def continue(): Game = copy(ok = active :: ok, active = nextPlayersMove())

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
        currentTurn.neighbour,
        getNeighbour(currentTurn.neighbour)),
        tmpDeck._2)
    } else {
      (new Turn(currentTurn.neighbour,
        getNeighbour(currentTurn.neighbour),
        getNeighbour(getNeighbour(currentTurn.neighbour))),
        tmpDeck._2)
    }
  }

  def takeCards(): Game = active match {
    case x if x.equals(currentTurn.victim) =>
      active.pickCards(currentTurn.getCards)
      active.sortHandCards
      val (nextTurn, newDeck) = closeTurn(false)
      copy(currentTurn = nextTurn, active = currentTurn.neighbour, deck = newDeck)
    case _ => throw new NoCardsToTakeException() // nonsense action; ignored in tui, blocked in gui
  }

  def playCard(card: Option[Card], cardToBlock: Option[Card]): Game = card match {
    case Some(c) =>
      if (active.hasCard(c)) {
        active match {
          case x if x.equals(currentTurn.victim) =>
            if (cardToBlock.isEmpty) {
              shove(c)
            } else {
              defend(c, cardToBlock)
            }
          case y if (y.equals(currentTurn.attacker)
            || y.equals(currentTurn.neighbour)) =>
            if (currentTurn.attackCards.size < currentTurn.victim.handCards.size) {
              attack(c)
            } else {
              throw new VictimHasNotEnoughCardsToBlockException()
            }
        }
      } else {
        this // player does not have card.. punish him!
      }
    case None => continue()
  }

  def defend(card: Card, cardToBlock: Option[Card]): Game = cardToBlock match {
    case Some(enemy) =>
      if (checkBlockCard(card, enemy)) {
        active.dropCards(card :: Nil)
        if (currentTurn.addBlockCard(enemy, card).attackCards.isEmpty) {
          if (active.handCards.nonEmpty) {
            copy(ok = Nil, currentTurn = currentTurn.addBlockCard(enemy, card), active = nextPlayersMove())
          } else {
            defendAndSetWinner()
          }
        } else {
          println("2")
          copy(currentTurn = currentTurn.addBlockCard(enemy, card), ok = Nil)
        }
      } else {
        throw new IllegalTurnException()
      }
    case None => throw new MissingBlockingCardException()
  }

  def defendAndSetWinner(): Game = {
    if (winner.isEmpty) {
      if (players.size == 2) {
        copy(players = players.filterNot(p => p.equals(active)), winner = Some(active))
      } else {
        val newPlayers: List[Player] = players.filterNot(p => p.equals(active))
        if (newPlayers.size == 2) {
          copy(players = newPlayers, active = getNeighbour(active), winner = Some(active),
            currentTurn = new Turn(getNeighbour(active), getNeighbour(getNeighbour(active)),
              getNeighbour(active)))
        } else {
          copy(players = newPlayers, active = getNeighbour(active), winner = Some(active),
            currentTurn = new Turn(getNeighbour(active), getNeighbour(getNeighbour(active)),
              getNeighbour(getNeighbour(getNeighbour(active)))))
        }
      }
    } else {
      if (players.size == 2) {
        copy(players.filterNot(p => p.equals(active)))
      } else {
        val newPlayers: List[Player] = players.filterNot(p => p.equals(active))
        if (newPlayers.size == 2) {
          copy(ok = Nil, players = newPlayers, currentTurn = new Turn(getNeighbour(active),
            getNeighbour(getNeighbour(active)), getNeighbour(active)), active = getNeighbour(active))
        } else {
          copy(ok = Nil, players = newPlayers, currentTurn = new Turn(getNeighbour(active),
            getNeighbour(getNeighbour(active)), getNeighbour(getNeighbour(getNeighbour(active)))),
            active = getNeighbour(active))
        }
      }
    }
  }

  def attack(card: Card): Game = if (checkAttackCard(card)) {
    active.dropCards(card :: Nil)

    val newAttacker: Player = getNeighbour(currentTurn.victim)
    val newAttackCards: List[Card] = currentTurn.addAttackCard(card).attackCards
    val newTurn: Turn = Turn(newAttacker, currentTurn.victim, newAttacker, newAttackCards, Map())

    if (active.handCards.nonEmpty) {
      copy(ok = Nil, currentTurn = currentTurn.addAttackCard(card))
    } else if (winner.isEmpty) {
      if (players.size == 2) {
        copy(ok = Nil, players = players.filterNot(p => p.equals(active)), winner = Some(active))
      } else {
        copy(ok = Nil, players = players.filterNot(p => p.equals(active)), currentTurn = newTurn,
          active = currentTurn.victim, winner = Some(active))
      }
    } else {
      copy(ok = Nil, players = players.filterNot(p => p.equals(active)), currentTurn = newTurn, active = currentTurn.victim)
    }
  } else {
    throw new IllegalTurnException()
  }

  def shove(card: Card): Game = {
    if (getNeighbour(active).handCards.size >= currentTurn.attackCards.size + 1) {
      if (card.value.equals(currentTurn.attackCards.head.value) && currentTurn.blockedBy.isEmpty) {
        currentTurn.victim.dropCards(card :: Nil)
        if (currentTurn.victim.handCards.isEmpty) {
          if (winner.isEmpty) {
            if (players.size == 2) {
              copy(players = players.filterNot(p => p.equals(active)), winner = Some(active))
            } else {
              val newPlayers: List[Player] = players.filterNot(p => p.equals(active))
              if (newPlayers.size == 2) {
                copy(players = newPlayers, winner = Some(active), active = getNeighbour(active),
                  currentTurn = Turn(getNeighbour(getNeighbour(active)), getNeighbour(active),
                    getNeighbour(getNeighbour(active)), currentTurn.addAttackCard(card).attackCards, Map()))
              } else {
                copy(players = newPlayers, winner = Some(active), active = getNeighbour(active),
                  currentTurn = Turn(getNeighbour(getNeighbour(active)), getNeighbour(active),
                    getNeighbour(getNeighbour(getNeighbour(active))), currentTurn.addAttackCard(card).attackCards, Map()))
              }
            }
          } else {
            val newPlayers = players.filterNot(p => p.equals(active))
            if (newPlayers.size == 2) {
              copy(players = newPlayers, active = getNeighbour(getNeighbour(active)),
                currentTurn = Turn(getNeighbour(active), getNeighbour(getNeighbour(active)), getNeighbour(active),
                  currentTurn.addAttackCard(card).attackCards, Map()))
            } else {
              copy(players = newPlayers, active = getNeighbour(getNeighbour(active)),
                currentTurn = Turn(getNeighbour(getNeighbour(active)), getNeighbour(active),
                  getNeighbour(getNeighbour(getNeighbour(active))), currentTurn.addAttackCard(card).attackCards, Map()))
            }
          }
        } else {
          copy(ok = Nil, active = getNeighbour(currentTurn.victim), currentTurn = Turn(currentTurn.victim,
            getNeighbour(currentTurn.victim), getNeighbour(getNeighbour(currentTurn.victim)),
            card :: currentTurn.attackCards, Map()))
        }
      } else {
        throw new IllegalTurnException()
      }
    } else {
      throw new VictimHasNotEnoughCardsToBlockException()
    }
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

  def checkAttackCard(card: Card): Boolean = {
    if (currentTurn.attackCards.isEmpty && currentTurn.blockedBy.isEmpty) {
      true
    } else {
      val defended = currentTurn.blockedBy.keys.toList
      val defendedBy = currentTurn.blockedBy.values.toList
      val notDefended = currentTurn.attackCards
      (notDefended ::: defended ::: defendedBy).foreach(c => {
        if (c.value.equals(card.value)) {
          return true
        }
      })
      false
    }
  }

  def nextPlayersMove(): Player = active match {
    case x if x.equals(currentTurn.neighbour)
      && !currentTurn.attacker.equals(currentTurn.neighbour) => currentTurn.attacker
    case _ => getNeighbour(active)
  }

  def getNeighbour(player: Player): Player = players.indexOf(player) match {
    case 0 => players.last
    case _ => players(players.indexOf(player) - 1)
  }

  def computePossibilities(): Either[List[Card], Map[Card, Card]] = {
    if (active.equals(currentTurn.attacker) || active.equals(currentTurn.neighbour)) {
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
}
