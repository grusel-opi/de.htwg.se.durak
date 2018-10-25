package de.htwg.se.durak

import de.htwg.se.durak.model._

import scala.io.StdIn._

object Main {

  val commands = Set("newPlayer", "card", "exit")
  val shuffledDeck: Deck = new Deck().shuffle
  var playerList: List[Player] = List()

  def main(args: Array[String]): Unit = {
    val v = new Card(CardColor.Clubs, CardValue.Eight)

    val c = new Card(CardColor.Clubs, CardValue.Ace)

    print(v.compare(c))
  }
}
