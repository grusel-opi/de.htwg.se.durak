package de.htwg.se.durak.controller

import de.htwg.se.durak.model._
import de.htwg.se.durak.util.Observable

class Controller() extends Observable {

  var players: List[Player] = Nil
  var wrappedAttacker: Option[Player] = None
  var wrappedVictim: Option[Player] = None

  var attackCards: List[Card] = Nil
  var blockCards: List[Card] = Nil

  var wrappedDurakGame: Option[Desk] = None
  var gameFinished: Boolean = false

  var wrappedTurn: Option[Turn] = None

  def createNewGame(numberOfPlayers: Int, playerNames: List[String]): Unit = {
    var wrappedCardDeckTuple: Option[(Card, Deck)] = Some(new Deck().shuffle.popTopCard())
    val cardDeckTuple: (Card, Deck) = {
      wrappedCardDeckTuple match {
        case None => {
          throw new MatchError("wrappedCardDeckTuple is None!!!")
        }
        case _ => wrappedCardDeckTuple.get
      }
    }
    val trump: Card = cardDeckTuple._1
    var deck: Deck = cardDeckTuple._2
    wrappedCardDeckTuple = None

    var handCard: List[Card] = Nil

    for (i <- 0 until numberOfPlayers) {

      var handCards: List[Card] = Nil

      for (j <- 0 to 4) {
        wrappedCardDeckTuple match {
          case None => {
            wrappedCardDeckTuple = Some(deck.popTopCard())
          }
          case _ => throw new MatchError("cardDeckTuple should be None before overriding!")
        }

        handCards = wrappedCardDeckTuple.get._1 :: handCards
        deck = wrappedCardDeckTuple.get._2
        wrappedCardDeckTuple = None
      }

      players = new Player(playerNames(i), handCards) :: players
    }

    wrappedDurakGame = Some(Desk(players, deck, trump, None))
    wrappedAttacker = Some(players(0))
    wrappedVictim = Some(players(1))
  }

  def addAttackCard(idx: Int): Unit = {
    val attacker: Player = wrappedAttacker match {
      case None => throw new MatchError("ILLEGAL! Attacker should not be None!!!")
      case _ => wrappedAttacker.get
    }

    if (attackCards.isEmpty) {
      attackCards = attacker.handCards(idx) :: attackCards
    } else {
      if (attackCards(0).value.compare(attacker.handCards(idx).value) == 0) {
        println("attackCards.value: " + attackCards(0).value)
        println("choosedCard.value: " + attacker.handCards(idx).value)
      } else {
        println("cannot add card: " + attacker.handCards(idx) + " to attack cards! Attack cards conataing cards with " +
          "other value")
      }
    }
  }

  def addBlockCard(blockCardIdx: Int, attackCardIdx: Int): Boolean = {
    val victim = wrappedVictim match {
      case None => throw new MatchError("ILLEGAL! Victim should not be None!!!")
      case _ => wrappedVictim.get
    }

    val blockCard: Card = victim.handCards(blockCardIdx)
    val attackCard: Card = attackCards(attackCardIdx)

    if (blockCard.value.compare(attackCard.value) > 0) {
      blockCards = blockCard :: blockCards
      true
    }

    false
  }

  def getPossibleBlockingCards: List[Card] = {
    var possibleBlockingCards: List[Card] = Nil

    for (attackCard <- attackCards) {
      for (possibleBlockingCard <- wrappedVictim.get.handCards) {
        if (possibleBlockingCard.value.compare(attackCard.value) > 0 && !possibleBlockingCards.contains(possibleBlockingCard)) {
          possibleBlockingCards = possibleBlockingCard :: possibleBlockingCards
        } else if (possibleBlockingCard.color == wrappedDurakGame.get.trump.color) {
          possibleBlockingCards = possibleBlockingCard :: possibleBlockingCards
        }
      }
    }

    possibleBlockingCards
  }

  def possibleBlockingCardsToString: String = {
    getPossibleBlockingCards.mkString(", ")
  }

  def victimTakeAttackCards: Unit = {
    val victim: Player = wrappedVictim match {
      case None => throw new MatchError("ILLEGAL! Victim should not be None!!!")
      case _ => wrappedVictim.get
    }

    println("HIER hand cards before: " + victim.handCards)
    val newVictim = victim.pickCards(attackCards)
    println("HIER hand cards after: " + newVictim.handCards)
    wrappedVictim = Some(newVictim)
    attackCards = Nil
  }

  def removeCardsFromAttackerHand: Unit = {
    val attacker: Player = wrappedAttacker match {
      case None => throw new MatchError("ILLEGAL! wrappedAttacker should not be None!!!")
      case _ => wrappedAttacker.get
    }

    println("HIER cards before: " + attacker.handCards.mkString(", "))
    val newAttacker: Player = attacker.removeCards(attackCards)
    println("HIER cards after: " + newAttacker.handCards.mkString(", "))

    wrappedAttacker = Some(newAttacker)
  }
}
