package de.htwg.se.durak.view.gui.sceneControllers

import de.htwg.se.durak.view.gui.Gui
import scalafx.scene.control.Button
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class WinningGameSceneController(private val winningPlayerNameText: Text,
                                 private val backToMainMenuButton: Button) {

  setWinningPlayerText()

  def backToMainMenuButtonPressed(): Unit = {
    //Gui.resetData()
    Gui.displayMainMenuScene()
  }

  def setWinningPlayerText(): Unit = {
    winningPlayerNameText.setText(Gui.controller.game.winner.get + " has won the game.")
  }
}
