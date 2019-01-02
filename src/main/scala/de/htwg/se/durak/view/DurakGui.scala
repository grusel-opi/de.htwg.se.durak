package de.htwg.se.durak.view

import de.htwg.se.durak.view.gui.SFXGui
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml
import scala.swing.Reactor


@sfxml
class DurakGui(private val newGameButton: Button) extends Reactor {

  def newGameButtonPressed(): Unit = {
    SFXGui.displayNewGameScene()
  }

}
