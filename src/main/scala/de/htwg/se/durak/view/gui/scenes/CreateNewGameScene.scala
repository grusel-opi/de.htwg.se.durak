package de.htwg.se.durak.view.gui.scenes

import de.htwg.se.durak.view.gui.Gui
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, TextField}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class CreateNewGameScene(private val numberOfPlayersText: Text,
                         private val playerNameTextField: TextField) {

  numberOfPlayersText.setText(Gui.controller.players.size.toString)

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

  def addPlayerButtonPressed(): Unit = {
    val playerName: String = playerNameTextField.getText

    if (!playerName.equals("")) {
      Gui.controller.newPlayer(playerName);
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
}
