package de.htwg.se.durak.model.model.gameComponent

import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.durak.model.turnComponent.TurnInterface
import de.htwg.se.durak.model.turnComponent.turnBaseImpl.Turn
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TurnSpec extends WordSpec with Matchers {
  "A Turn" when {

    val attacker: Player = new Player("Kai")
    val victim: Player = new Player("Lutscho")
    val neighbor: Player = new Player("Lukas")

    "created" should {
      val attackCard1: CardInterface = Card(CardColor.Herz, CardValue.König)
      val attackCard2: CardInterface = Card(CardColor.Pik, CardValue.König)
      val attackCard3: CardInterface = Card(CardColor.Kreuz, CardValue.König)
      val attackCard4: CardInterface = Card(CardColor.Karo, CardValue.König)

      val blockingCard1: CardInterface = Card(CardColor.Herz, CardValue.König)
      val blockingCard2: CardInterface = Card(CardColor.Pik, CardValue.König)
      val blockingCard3: CardInterface = Card(CardColor.Kreuz, CardValue.König)
      val blockingCard4: CardInterface = Card(CardColor.Karo, CardValue.König)

      val attackCards: List[CardInterface] = List(attackCard1, attackCard2, attackCard3, attackCard4)
      val blockingCards: List[CardInterface] = List(blockingCard1, blockingCard2, blockingCard3, blockingCard4)
      val blockedBy: Map[CardInterface, CardInterface] = Map((attackCard1, blockingCard1), (attackCard2, blockingCard2),
        (attackCard3, blockingCard3), (attackCard4, blockingCard4))

      val turn: TurnInterface = Turn(attacker, victim, neighbor, attackCards, blockedBy)

      "have a attacker." in {
        turn.attacker should be(attacker)
        turn.attacker.name should be(attacker.name)
        turn.attacker.handCards should be(Nil)
      }

      "have a vicitm." in {
        turn.victim should be(victim)
        turn.victim.name should be(victim.name)
        turn.victim.handCards should be(Nil)
      }

      "have a neighbor." in {
        turn.victim should be(victim)
        turn.victim.name should be(victim.name)
        turn.victim.handCards should be(Nil)
      }

      "have attack cards." in {
        turn.attackCards.size should be(attackCards.size)
        turn.attackCards should be(attackCards)
      }

      "have cards, wich block the attack cards." in {
        turn.blockedBy.size should be(blockingCards.size)
        turn.blockedBy should be(blockedBy)
      }

      "have a nice string representation." in {
        val attackerStringRepresentation: String = "Attacker: " + attacker.name + "\n"
        val victimStringRepresentation: String = "Defender: " + victim.name + "\n"
        val neighborStringRepresentation: String = "Neighbor: " + neighbor.name + "\n"
        val attackCardsStringRepresentation: String = "Cards to block: " + attackCards.mkString("; ") + "\n"
        val blockingCardsStringRepresentation: String = "Blocked Cards: " + blockedBy.mkString("; ")

        val turnStringRepresentation: String = attackerStringRepresentation + victimStringRepresentation +
          neighborStringRepresentation + attackCardsStringRepresentation + blockingCardsStringRepresentation

        turn.toString should be(turnStringRepresentation)
      }
    }

    "created without attack cards and without cards which block attack cards" should {

      val turn: TurnInterface = new Turn(attacker, victim, neighbor)

      "have a attacker." in {
        turn.attacker should be(attacker)
        turn.attacker.name should be(attacker.name)
        turn.attacker.handCards should be(Nil)
      }

      "have a victim." in {
        turn.victim should be(victim)
        turn.victim.name should be(victim.name)
        turn.victim.handCards should be(Nil)
      }

      "have a neighbor." in {
        turn.victim should be(victim)
        turn.victim.name should be(victim.name)
        turn.victim.handCards should be(Nil)
      }

      "have empty attack cards." in {
        turn.attackCards.size should be(0)
        turn.attackCards should be(Nil)
      }

      "have emtpy cards which block attack cards." in {
        turn.blockedBy.size should be(0)
        turn.blockedBy.isEmpty should be(true)
      }

      "have a nice string representation." in {
        val attackerStringRepresentation: String = "Attacker: " + attacker.name + "\n"
        val victimStringRepresentation: String = "Defender: " + victim.name + "\n"
        val neighborStringRepresentation: String = "Neighbor: " + neighbor.name + "\n"
        val attackCardsStringRepresentation: String = "Cards to block: " + turn.attackCards.mkString("; ") + "\n"
        val blockingCardsStringRepresentation: String = "Blocked Cards: " + turn.blockedBy.mkString("; ")

        val turnStringRepresentation: String = attackerStringRepresentation + victimStringRepresentation +
          neighborStringRepresentation + attackCardsStringRepresentation + blockingCardsStringRepresentation

        turn.toString should be(turnStringRepresentation)
      }
    }

    "add a attack card to empty attack cards" should {
      val cardToAdd: CardInterface = Card(CardColor.Herz, CardValue.Fünf)

      val turn: TurnInterface = Turn(attacker, victim, neighbor, Nil, Map())

      "have one attack card." in {
        turn.attackCards.size should be(0)
        turn.attackCards should be(Nil)

        val newTurn = turn.addAttackCard(cardToAdd)

        newTurn.attackCards.size should be(1)
        newTurn.attackCards should be(List(cardToAdd))
      }
    }

    "add a attack card to other attack cards" should {
      val cardToAdd: CardInterface = Card(CardColor.Kreuz, CardValue.Acht)
      val existingAttackCard: CardInterface = Card(CardColor.Karo, CardValue.Acht)
      val attackCards: List[CardInterface] = List(existingAttackCard)

      val turn: TurnInterface = Turn(attacker, victim, neighbor, attackCards, Map())
      "have one attack card more as before" in {
        turn.attackCards.size should be(attackCards.size)
        turn.attackCards should be(attackCards)

        val newTurn: TurnInterface = turn.addAttackCard(cardToAdd)

        newTurn.attackCards.size should be(attackCards.size + 1)
        newTurn.attackCards should be (cardToAdd::attackCards)
      }
    }

    "add a blocking card, which beat one of the attack cards" should {

      val attackCard: CardInterface = Card(CardColor.Herz, CardValue.Fünf)
      val attackCards: List[CardInterface] = List(attackCard)

      val turn: TurnInterface = Turn(attacker, victim, neighbor, attackCards, Map())

      "have one card, blocking one of the attack cards." in {
        turn.attackCards.size should be(attackCards.size)
        turn.attackCards should be(attackCards)
        turn.blockedBy.size should be(0)
        turn.blockedBy.isEmpty should be(true)

        val blockingCard: CardInterface = Card(CardColor.Herz, CardValue.Sechs)
        val newTurn: TurnInterface = turn.addBlockCard(attackCard, blockingCard)

        newTurn.attackCards.size should be(attackCards.size - 1)
        newTurn.attackCards should be(Nil)
        newTurn.blockedBy.size should be(1)
        newTurn.blockedBy.isEmpty should be(false)
      }
    }

    "trying to get all cards" should {

      "return no cards, if attack cards and blocking cards are empty." in {
        val turn: TurnInterface = new Turn(attacker, victim, neighbor)

        turn.getCards.size should be(0)
        turn.getCards should be(Nil)
      }

      "return only attack cards if blocking cards are empty." in {
        val attackCard1: CardInterface = Card(CardColor.Kreuz, CardValue.König)
        val attackCard2: CardInterface = Card(CardColor.Pik, CardValue.König)

        val attackCards: List[CardInterface] = List(attackCard1, attackCard2)
        val turn: TurnInterface = Turn(attacker, victim, neighbor, attackCards, Map())

        turn.getCards.size should be(2)
        turn.getCards should be(attackCards)
      }

      "return attack cards and blocking cards, if they're not empty." in {
        val attackCard1: CardInterface = Card(CardColor.Herz, CardValue.König)
        val attackCard2: CardInterface = Card(CardColor.Pik, CardValue.König)
        val attackCard3: CardInterface = Card(CardColor.Kreuz, CardValue.König)
        val attackCard4: CardInterface = Card(CardColor.Karo, CardValue.König)

        val blockingCard1: CardInterface = Card(CardColor.Herz, CardValue.König)
        val blockingCard2: CardInterface = Card(CardColor.Pik, CardValue.König)
        val blockingCard3: CardInterface = Card(CardColor.Kreuz, CardValue.König)
        val blockingCard4: CardInterface = Card(CardColor.Kreuz, CardValue.König)

        val attackCards: List[CardInterface] = List(attackCard1, attackCard2, attackCard3, attackCard4)
        val blockingCards: List[CardInterface] = List(blockingCard1, blockingCard2, blockingCard3, blockingCard4)
        val blockedBy: Map[CardInterface, CardInterface] = Map((attackCard1, blockingCard1), (attackCard2, blockingCard2),
          (attackCard3, blockingCard3), (attackCard4, blockingCard4))

        val turn: TurnInterface = Turn(attacker, victim, neighbor, attackCards, Map())
        var newTurn: TurnInterface = turn.addBlockCard(attackCard1, blockingCard1)
        newTurn = newTurn.addBlockCard(attackCard2, blockingCard2)
        newTurn = newTurn.addBlockCard(attackCard3, blockingCard3)
        newTurn = newTurn.addBlockCard(attackCard4, blockingCard4)

        newTurn.getCards.size should be(attackCards.size + blockingCards.size)
        newTurn.getCards should be(Nil ::: blockedBy.values.toList ::: blockedBy.keys.toList)
      }
    }
  }
}
