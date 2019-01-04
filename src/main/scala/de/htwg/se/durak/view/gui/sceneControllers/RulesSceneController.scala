package de.htwg.se.durak.view.gui.sceneControllers

import de.htwg.se.durak.view.gui.Gui
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml

@sfxml
class RulesSceneController(private val mainMenuButton: Button) {

  def mainMenuButtonPressed(): Unit = {
    Gui.displayMainMenuScene()
  }
}
