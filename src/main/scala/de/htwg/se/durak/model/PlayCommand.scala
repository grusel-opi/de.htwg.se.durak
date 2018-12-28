package de.htwg.se.durak.model

import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.util.Command

class PlayCommand(firstCard: Option[Card], secondCard: Option[Card], controller: Controller) extends Command {

  var memento: DurakGame = controller.game
  var cardMemento: List[Card] = controller.game.active.handCards

  override def doStep(): Unit = {
    memento = controller.game
    cardMemento = controller.game.active.handCards
    controller.game = controller.game.playCard(firstCard, secondCard)
  }

  override def undoStep(): Unit = {
    switchState()
  }

  override def redoStep(): Unit = {
    switchState()
  }

  def switchState(): Unit = {
    val newMemento = controller.game
    val newCardMemento = controller.game.active.handCards
    controller.game = memento
    controller.game.active.handCards = cardMemento
    memento = newMemento
    cardMemento = newCardMemento
  }
}
