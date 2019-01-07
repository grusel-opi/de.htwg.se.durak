package de.htwg.se.durak.view.gui.sceneControllers

import de.htwg.se.durak.view.gui.Gui
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, CheckBox, TextField}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class NewGameSceneController(private val playerNameTextField: TextField,
                             private val addPlayerButton: Button,
                             private val numberOfPlayersText: Text,
                             private val bigDeckCheckBox: CheckBox,
                             private val mainMenuButton: Button,
                             private val startGameButton: Button,
                             private val resetButton: Button) {

  setNumberOfPlayersText()

  def setNumberOfPlayersText(): Unit = {
    numberOfPlayersText.setText(Gui.controller.players.size.toString)
  }

  def addPlayerButtonPressed(): Unit = {
    val playerName: String = playerNameTextField.getText

    if (!playerName.equals("")) {
      Gui.controller.newPlayer(playerName)
      numberOfPlayersText.setText(Gui.controller.players.size.toString)
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
    val numberOfPlayers: Int = Gui.controller.players.size

    if (numberOfPlayers < 2) {
      new Alert(AlertType.Warning) {
        title = "Warning Dialog"
        headerText = "Warning: Too few players!"
        contentText = "Please add another player first."
      }.showAndWait()
    } else {
      Gui.controller.newGame()
      Gui.gameStarted = true
      Gui.displayGameScene()
    }
  }

  def mainMenuButtonPressed(): Unit = {
    Gui.displayMainMenuScene()
  }

  def resetButtonPressed(): Unit = {
    Gui.controller.players = Nil
    Gui.displayNewGameScene()
  }

  def bigDeckCheckBoxPressed(): Unit = {
    if (bigDeckCheckBox.isSelected) {
      println("is selected")
      bigDeckCheckBox.setSelected(true)
    } else {
      println("is not selected")
      bigDeckCheckBox.setSelected(false)
    }
  }

}
