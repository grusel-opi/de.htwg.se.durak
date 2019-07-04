package de.htwg.se.durak.aview.gui.sceneControllers

import de.htwg.se.durak.Durak
import de.htwg.se.durak.aview.gui.Gui
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml

@sfxml
class RulesSceneController(private val mainMenuButton: Button) {

  val gui: Gui = Durak.gui

  def mainMenuButtonPressed(): Unit = {
    gui.displayMainMenuScene()
  }
}
