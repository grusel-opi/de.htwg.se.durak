package de.htwg.se.durak.model

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec with Matchers {
  "A Player" when { "new" should {
    val player: Player = Player("Player Name", Nil)
    "have a name" in {
      player.name should be("Player Name")
    }
    "have a nice string representation" in {
      player.toString should be("Player Name")
    }
  }}
}
