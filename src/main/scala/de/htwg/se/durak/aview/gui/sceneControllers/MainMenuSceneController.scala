package de.htwg.se.durak.aview.gui.sceneControllers

import de.htwg.se.durak.Durak
import de.htwg.se.durak.aview.gui.Gui
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml

import scala.swing.Reactor

@sfxml
class MainMenuSceneController(private val newGameButton: Button,
                              private val loadGameButton: Button,
                              private val rulesButton: Button,
                              private val exitButton: Button) extends Reactor {
  val gui: Gui = Durak.gui

  def newGameButtonPressed(): Unit = {
    gui.displayNewGameScene()
  }

  def loadGameButtonPressed(): Unit = {
    gui.displayLoadGameScene()
  }

  def rulesButtonPressed(): Unit = {
    gui.displayRulesScene()
  }

  def exitButtonPressed(): Unit = {
    gui.exitGame()
  }

}
