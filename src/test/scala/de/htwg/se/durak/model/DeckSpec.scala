package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DeckSpec extends WordSpec with Matchers {
  "A Deck" when {
    "created" should {
      val deck: Deck = new Deck()
      "have 52 cards" in {
        deck.cards.length should be(52)
      }
    }
  }
}
