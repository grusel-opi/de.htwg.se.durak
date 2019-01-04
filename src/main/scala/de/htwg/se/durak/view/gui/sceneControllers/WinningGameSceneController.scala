package de.htwg.se.durak.view.gui.sceneControllers

import de.htwg.se.durak.view.gui.Gui
import scalafx.scene.control.Button
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class WinningGameSceneController(private val winningPlayerNameText: Text,
                                 private val backToMainMenuButton: Button) {

  // TODO: Replace trough winning player!
  winningPlayerNameText.setText("Somebody")

  def backToMainMenuButtonPressed(): Unit = {
    Gui.displayMainMenuScene()
  }
}
