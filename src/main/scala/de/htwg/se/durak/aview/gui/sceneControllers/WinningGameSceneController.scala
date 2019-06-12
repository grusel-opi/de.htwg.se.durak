package de.htwg.se.durak.aview.gui.sceneControllers

import de.htwg.se.durak.Durak
import de.htwg.se.durak.aview.gui.Gui
import scalafx.scene.control.Button
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class WinningGameSceneController(private val winningPlayerNameText: Text,
                                 private val backToMainMenuButton: Button) {

  val gui: Gui = Durak.gui
  setWinningPlayerText()

  def backToMainMenuButtonPressed(): Unit = {
    gui.controller.resetPlayers()
    gui.displayMainMenuScene()
  }

  def setWinningPlayerText(): Unit = {
    winningPlayerNameText.setText(gui.controller.winnersToString() + " has won the game.")
  }
}
