package de.htwg.se.durak.util

import de.htwg.se.durak.model._


object TestBcWorksheetsAreShit {


  def main(args: Array[String]): Unit = {
    var game = new DurakGame(List(new Player("1"), new Player("2"), new Player("3")))
    game = game.start()
    println("Trump: " + game.trump.toString())


    println("active: " + game.active.toString)
    val ret = game.computePossibilities().left.get.head
    println(ret.toString())

    game = game.playCard(Some(ret), None)
    game = game.playCard(None, None)
    println("active: " + game.active.toString)
    println(game.active.handCards.sorted(game.CardOrdering).mkString(", "))



    println(game.computePossibilities())





  }
}
