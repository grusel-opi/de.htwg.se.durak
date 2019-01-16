package de.htwg.se.durak.model.gameComponent.gameBaseImpl

import de.htwg.se.durak.controller.controllerComponent.GameOverEvent
import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.model.playerComponent.Player
import de.htwg.se.durak.util.customExceptions._

import scala.util.Random

case class Game(players: List[Player], deck: Deck, trump: Card, currentTurn: Turn, active: Player, winners: List[Player]) extends GameInterface {

  def this(players: List[Player], deck: Deck) = this(players, deck, deck.cards.last,
    new Turn(players.head, players.head, players.head), players.head, Nil)

  def this(players: List[Player]) = this(players, new Deck().shuffle)

  def this() = this(List(new Player("default")))

  val NUMBER_OF_HAND_CARDS = 5

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

  def start(): Game = {
    val newDeck = distributeCards(players)
    val beginner: Player = players(math.abs(Random.nextInt()) % players.size)
    val firstVictim: Player = getNeighbour(beginner)
    val fistNeighbor: Player = getNeighbour(firstVictim)
    val newTurn = Turn(beginner, firstVictim, fistNeighbor, Nil, Map[Card, Card]())

    copy(deck = newDeck, currentTurn = newTurn, active = beginner)
  }

  def distributeCards(people: List[Player]): Deck = {
    var tmpDeck = (List[Card](), deck)
    people.foreach(p => {
      val missingAmount = NUMBER_OF_HAND_CARDS - p.handCards.size
      if (missingAmount > 0) {
        tmpDeck = tmpDeck._2.popNCards(missingAmount)
        p.pickCards(tmpDeck._1)
        p.sortHandCards
      }
    })
    tmpDeck._2
  }

  def playOk(): Game = active match {
    case x if x.equals(currentTurn.attacker) =>
      if (currentTurn.attackCards.nonEmpty) {
        copy(active = nextPlayersMove())
      } else if (currentTurn.blockedBy.nonEmpty) {
        val (newTurn, newDeck) = closeTurn(true)
        copy(active = nextPlayersMove(), currentTurn = newTurn, deck = newDeck)
      } else {
        throw new LayCardFirsException
      }
    case x if x.equals(currentTurn.neighbour) =>
      copy(active = nextPlayersMove())
  }

  def closeTurn(success: Boolean): (Turn, Deck) = { //dont forget to set new active on every usage!
    val newDeck = distributeCards(currentTurn.getPlayers)
    if (success) {
      (new Turn(currentTurn.victim,
        currentTurn.neighbour,
        getNeighbour(currentTurn.neighbour)),
        newDeck)
    } else {
      (new Turn(currentTurn.neighbour,
        getNeighbour(currentTurn.neighbour),
        getNeighbour(getNeighbour(currentTurn.neighbour))),
        newDeck)
    }
  }

  def takeCards(): Game = active match {
    case x if x.equals(currentTurn.victim) =>
      active.pickCards(currentTurn.getCards)
      active.sortHandCards
      val (nextTurn, newDeck) = closeTurn(false)
      copy(currentTurn = nextTurn, active = currentTurn.neighbour, deck = newDeck)
    case _ => throw new NoCardsToTakeException()
  }

  def playCard(card: Card, cardToBlock: Option[Card]): Game = {
    if (active.hasCard(card)) {
      active match {
        case x if x.equals(currentTurn.victim) =>
          if (cardToBlock.isEmpty) {
            shove(card)
          } else {
            defend(card, cardToBlock)
          }
        case y if y.equals(currentTurn.attacker) || y.equals(currentTurn.neighbour) =>
          if (currentTurn.attackCards.size < currentTurn.victim.handCards.size) {
            attack(card)
          } else {
            throw new VictimHasNotEnoughCardsToBlockException()
          }
      }
    } else {
      this // player does not have card.. punish him!
    }
  }

  def defend(card: Card, cardToBlock: Option[Card]): Game = cardToBlock match {
    case Some(enemy) =>
      if (checkBlockCard(card, enemy)) {
        active.dropCards(card :: Nil)
        if (currentTurn.addBlockCard(enemy, card).attackCards.isEmpty) {
          if (active.handCards.nonEmpty) {
            copy(currentTurn = currentTurn.addBlockCard(enemy, card), active = nextPlayersMove())
          } else {
            winByDefence()
          }
        } else {
          copy(currentTurn = currentTurn.addBlockCard(enemy, card))
        }
      } else {
        throw new IllegalTurnException()
      }
    case None => throw new MissingBlockingCardException()
  }

  def winByDefence(): Game = {
    val newWinners = active :: winners
    val newPlayers = players.filterNot(p => p.equals(active))
    if (newPlayers.size == 1) {
      copy(players = newPlayers, active = getNeighbour(active), winners = newWinners) // TODO: game over!
    } else if (newPlayers.size == 2) {
      copy(players = newPlayers, active = getNeighbour(active), winners = newWinners,
        currentTurn = new Turn(getNeighbour(active), getNeighbour(getNeighbour(active)), getNeighbour(active)))
    } else {
      copy(players = newPlayers, active = getNeighbour(active), winners = newWinners,
        currentTurn = new Turn(getNeighbour(active), getNeighbour(getNeighbour(active)), getNeighbour(getNeighbour(getNeighbour(active)))))
    }
  }

  def attack(card: Card): Game = if (checkAttackCard(card)) {
    active.dropCards(card :: Nil)
    if (active.handCards.nonEmpty) { // attacker won
      copy(currentTurn = currentTurn.addAttackCard(card))
    } else {
      val newWinners = active :: winners
      val newPlayers = players.filterNot(p => p.equals(active))
      if (newPlayers.size == 1) {
        copy(players = newPlayers, active = getNeighbour(active), winners = newWinners) // TODO: game over!
      } else if (newPlayers.size == 2) {
        copy(players = newPlayers, active = getNeighbour(active), winners = newWinners,
          currentTurn = Turn(getRightNeighbour(active), currentTurn.victim, getRightNeighbour(active),
            attackCards = card :: currentTurn.attackCards, blockedBy = currentTurn.blockedBy))
      } else {
        copy(players = newPlayers, active = getNeighbour(active), winners = newWinners,
          currentTurn = Turn(getRightNeighbour(active), currentTurn.victim, currentTurn.neighbour,
            attackCards = card :: currentTurn.attackCards, blockedBy = currentTurn.blockedBy))
      }
    }
  } else {
    throw new IllegalTurnException()
  }

  def shove(card: Card): Game = {
    if (isShovable(card)) {
      active.dropCards(card :: Nil)
      if (active.handCards.nonEmpty) {
        copy(currentTurn = Turn(active, currentTurn.neighbour, getNeighbour(currentTurn.neighbour),
          attackCards = card :: currentTurn.attackCards, blockedBy = currentTurn.blockedBy))
      } else { // active won
        val newWinners = active :: winners
        val newPlayers = players.filterNot(p => p.equals(active))
        newPlayers.size match {
          case 1 =>
            copy(players = newPlayers, active = getNeighbour(active), winners = newWinners) // TODO: game over!
          case 2 =>
            copy(players = newPlayers, active = getNeighbour(active), winners = newWinners,
              currentTurn = Turn(currentTurn.attacker, currentTurn.neighbour, currentTurn.attacker,
                attackCards = card :: currentTurn.attackCards, blockedBy = currentTurn.blockedBy))
          case _ =>
            copy(players = newPlayers, active = getNeighbour(active), winners = newWinners,
              currentTurn = Turn(currentTurn.attacker, currentTurn.neighbour, getNeighbour(currentTurn.neighbour),
                attackCards = card :: currentTurn.attackCards, blockedBy = currentTurn.blockedBy))
        }
      }
    } else {
      throw new IllegalTurnException()
    }
  }

  def isShovable(card: Card): Boolean = {
    if (currentTurn.neighbour.handCards.size > currentTurn.attackCards.size
      && currentTurn.blockedBy.isEmpty
      && card.value.equals(currentTurn.attackCards.head.value)) {
      true
    } else {
      false
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

  def getRightNeighbour(player: Player): Player = players.indexOf(player) match {
    case x if x.equals(players.size - 1) => players.head
    case _ => players(players.indexOf(player) + 1)
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
