package de.htwg.se.durak.util

import de.htwg.se.durak.model.DurakGame

class UndoManager {
  private var undoStack: List[Command]= Nil
  private var redoStack: List[Command]= Nil
//
//  def produceGameState(game: DurakGame): DurakGameState = {
//    val savedPlayers = game.players.clone()
//
//
//  }

  def doStep(command: Command) = {
    undoStack = command::undoStack
    command.doStep
  }
  def undoStep  = {
    undoStack match {
      case  Nil =>
      case head::stack => {
        head.undoStep
        undoStack=stack
        redoStack= head::redoStack
      }
    }
  }
  def redoStep = {
    redoStack match {
      case Nil =>
      case head::stack => {
        head.redoStep
        redoStack=stack
        undoStack=head::undoStack
      }
    }
  }
}
