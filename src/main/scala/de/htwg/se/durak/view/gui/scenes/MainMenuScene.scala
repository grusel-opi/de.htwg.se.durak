package de.htwg.se.durak.view.gui.scenes

import de.htwg.se.durak.view.gui.SFXGui
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml
import scala.swing.Reactor

@sfxml
class MainMenuScene(private val newGameButton: Button) extends Reactor {

  def newGameButtonPressed(): Unit = {
    SFXGui.displayNewGameScene()
  }

}
