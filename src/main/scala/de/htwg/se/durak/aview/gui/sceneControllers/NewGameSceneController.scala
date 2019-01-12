package de.htwg.se.durak.aview.gui.sceneControllers

import de.htwg.se.durak.Durak
import de.htwg.se.durak.aview.gui.Gui
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, CheckBox, TextField}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml


@sfxml
class NewGameSceneController(private val playerNameTextField: TextField,
                             private val addPlayerButton: Button,
                             private val numberOfPlayersText: Text,
                             private val mainMenuButton: Button,
                             private val startGameButton: Button,
                             private val resetButton: Button) {

  val gui: Gui = Durak.gui
  setNumberOfPlayersText()

  def setNumberOfPlayersText(): Unit = {
    val currentNumberOfPlayers = gui.controller.players.size.toString
    numberOfPlayersText.setText(currentNumberOfPlayers)
  }

  def addPlayerButtonPressed(): Unit = {
    val playerName: String = playerNameTextField.getText

    if (!playerName.equals("")) {
      gui.controller.newPlayer(playerName)
      numberOfPlayersText.setText(gui.controller.players.size.toString)
      playerNameTextField.setText("")
    } else {
      new Alert(AlertType.Warning) {
        title = "Warning Dialog"
        headerText = "Warning: Player name cannot be empty!"
        contentText = "Please enter a non empty player name."
      }.showAndWait()
    }
  }

  def startGameButtonPressed(): Unit = {
    gui.controller.newGame()
  }

  def mainMenuButtonPressed(): Unit = {
    gui.displayMainMenuScene()
  }

  def resetButtonPressed(): Unit = {
    gui.controller.resetPlayers()
  }
}
