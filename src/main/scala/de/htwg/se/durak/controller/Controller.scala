package de.htwg.se.durak.controller

import de.htwg.se.durak.model.{Card, Deck, DurakGame, Player}
import de.htwg.se.durak.util.Observable

class Controller() extends Observable {

  var durakGame: Option[DurakGame] = None
  var players: List[Player] = Nil

  def createNewGame(numberOfPlayers: Int, playerNames: List[String]): Unit = {
    var cardDeckTuple: Option[(Card, Deck)] = Some(new Deck().popTopCard())
    val trump: Card = cardDeckTuple.get._1
    var deck: Deck = cardDeckTuple.get._2
    cardDeckTuple = None

    var handCard: List[Card] = Nil

    for (i <- 0 to numberOfPlayers - 1) {

      var handCards: List[Card] = Nil

      for (j <- 0 to 3) {
        cardDeckTuple match {
          case None => cardDeckTuple = Some(deck.popTopCard())
          case _ => throw new MatchError("cardDeckTuple should be None before overriding!")
        }

        handCards = cardDeckTuple.get._1 :: handCards
        deck = cardDeckTuple.get._2
        cardDeckTuple = None
      }

      players = new Player(playerNames(i), handCards) :: players
    }

    durakGame = Some(DurakGame(players, deck, trump))
    notifyObservers
  }

  def durakGameToString: String = durakGame match {
    case None => "durakGame ist None!"
    case _ => durakGame.get.toString
  }
}
