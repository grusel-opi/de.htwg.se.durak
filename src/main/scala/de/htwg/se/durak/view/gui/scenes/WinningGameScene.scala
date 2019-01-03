package de.htwg.se.durak.view.gui.scenes

import de.htwg.se.durak.view.gui.SFXGui
import scalafx.scene.control.Button
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class WinningGameScene(private val winningPlayerNameText: Text,
                       private val backToMainMenuButton: Button) {

  // TODO: Replace trough winning player!
  winningPlayerNameText.setText("Somebody")

  def backToMainMenuButtonPressed(): Unit = {
    SFXGui.displayMainMenuScene()
  }
}
