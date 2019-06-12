package de.htwg.se.durak.aview.gui.sceneControllers

import de.htwg.se.durak.aview.gui.Gui
import scalafx.scene.control.{Alert, Button}
import scalafxml.core.macros.sfxml
import scalafx.scene.text.Text
import scalafx.stage.FileChooser
import java.io.File

import de.htwg.se.durak.Durak
import scalafx.scene.control.Alert.AlertType

@sfxml
class LoadGameSceneController(private val selectedGameFileText: Text,
                              private val selectGameFileButton: Button,
                              private val loadFileButton: Button,
                              private val mainMenuButton: Button) {
  val gui: Gui = Durak.gui

  var fileNameToLoad: String = ""

  def setSelectedGameFileText(fileName: String): Unit = {
    selectedGameFileText.setText(fileName)
  }

  def mainMenuButtonPressed(): Unit = {
    gui.displayMainMenuScene()
  }

  def selectGameFileButtonPressed(): Unit = {
    val fileChooser: FileChooser = new FileChooser()
    fileChooser.setTitle("Open a durak save file")
    val file: File = fileChooser.showOpenDialog(gui.stage)

    if (file != null) {
      setSelectedGameFileText(file.getName)
      fileNameToLoad = file.getName
    }
  }

  def loadFileButtonPressed(): Unit = {
    if (!fileNameToLoad.equals("")) {
      gui.controller.loadGame(fileNameToLoad)
    } else {
      new Alert(AlertType.Warning) {
        title = "File was not set!"
        headerText = "No file was selected!"
        contentText = "Please select a file first"
      }.showAndWait()
    }
  }
}
