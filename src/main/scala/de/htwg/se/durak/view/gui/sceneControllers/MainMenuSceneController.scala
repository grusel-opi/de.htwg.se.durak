package de.htwg.se.durak.view.gui.scenes

import de.htwg.se.durak.view.gui.Gui
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml
import scala.swing.Reactor

@sfxml
class MainMenuSceneController(private val newGameButton: Button,
                              private val rulesButton: Button,
                              private val exitButton: Button) extends Reactor {

  def newGameButtonPressed(): Unit = {
    Gui.displayNewGameScene()
  }

  def rulesButtonPressed(): Unit = {
    Gui.displayRulesScene()
  }

  def exitButtonPressed(): Unit = {
    Gui.exitGame()
  }

}
