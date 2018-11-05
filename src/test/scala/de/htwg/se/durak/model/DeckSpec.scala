package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DeckSpec extends WordSpec with Matchers {
  "A Deck" when {
    "created" should {
      val deck: Deck = new Deck()
      println("deck: " + deck)
      "have 52 cards" in {
        deck.cards.size should be(52)
      }
    }
  }
}
