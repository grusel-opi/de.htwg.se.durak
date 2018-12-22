package de.htwg.se.durak.util

import de.htwg.se.durak.model.{Card, CardColor, CardValue, Player}


object TestBcWorksheetsAreShit {

  def main(args: Array[String]): Unit = {

    val list1 = List(new Player("1"), new Player("2"),new Player("3"), new Player("4"))

    val card1 = Card(CardColor.Karo, CardValue.Acht)

    val player = list1.head
    player.pickCards(card1::Nil)


    val list2 = list1.updated(0, player)

  }
}
