import de.htwg.se.durak.model.{Card, CardColor, CardValue, Player}

val list1 = List(new Player("1"), new Player("2"),new Player("3"), new Player("4"))
list1.foreach(p => println(p.stringWithCards))

val card1 = Card(CardColor.Karo, CardValue.Acht)

val player = list1.head
val newPlayer = player.pickCards(card1::Nil)


val list2 = list1.updated(1, newPlayer)
list2.foreach(p => println(p.stringWithCards))
