package de.htwg.se.durak.aview.gui.sceneControllers

import de.htwg.se.durak.aview.gui.Gui
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml
import scalafx.scene.text.Text
import scalafx.stage.FileChooser
import java.io.File

import de.htwg.se.durak.Durak

@sfxml
class LoadGameSceneController(private val selectedGameFileText: Text,
                              private val selectGameFileButton: Button,
                              private val mainMenuButton: Button) {
  val gui: Gui = Durak.gui

  def setSelectedGameFileText(fileName: String): Unit = {
    selectedGameFileText.setText(fileName)
  }

  def mainMenuButtonPressed(): Unit = {
    gui.displayMainMenuScene()
  }

  def selectGameFileButtonPressed(): Unit = {
    println("Not implemented yet :(")
    val fileChooser: FileChooser = new FileChooser()
    fileChooser.setTitle("Open a durak save file")
    val file: File = fileChooser.showOpenDialog(gui.stage)

    setSelectedGameFileText(file.getName)
  }
}
