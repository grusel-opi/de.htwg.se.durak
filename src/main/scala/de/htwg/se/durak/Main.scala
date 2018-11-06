package de.htwg.se.durak

import de.htwg.se.durak.model.Deck
import de.htwg.se.durak.view.TUI

import scala.io.StdIn._

object Main {
  def main(args: Array[String]): Unit = {
    val d0 = new Deck().shuffle
    println("size: " + d0.cards.size)
    var i = 0
    d0.cards.foreach(c => {
      println("card no. " + i + " = " + c.toString())
      i += 1
    })
    val (card0, d1) = d0.popTopCard()
    val (card1, d2) = d1.popTopCard()
    println("card0: " + card0.toString())
    println("card1: " + card1.toString())
    println("Compare: " + card0.value.compare(card1.value))
  }

}
