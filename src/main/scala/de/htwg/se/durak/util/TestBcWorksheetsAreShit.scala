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

    var r = game.playCard(Some(ret), None)
    r = r._2.playCard(None, None)
    println("active: " + r._2.active.toString)
    println(r._2.active.handCards.sortWith((c0, c1) => r._2.checkBlockCard(c0, c1)).reverse.mkString(", "))



    println(r._2.computePossibilities())





  }
}
