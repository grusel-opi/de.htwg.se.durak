package de.htwg.se.durak.view.gui


import java.io.IOException

import de.htwg.se.durak.controller.Controller
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLView, NoDependencyResolver}

import scala.swing.Reactor

object SFXGui extends JFXApp with Reactor {

  var gameStarted: Boolean = false
  var controller: Controller = _

  def setController(controller: Controller): Unit = {
    this.controller = controller
    listenTo(controller)
  }

  stage = new PrimaryStage() {
    title = "Durak"
    scene = new Scene(FXMLView(getClass.getResource("/mainMenuScene.fxml"), NoDependencyResolver))
  }

  def displayNewGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/createNewGameScene.fxml"), NoDependencyResolver))
  }

  def displayGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/gameScene.fxml"), NoDependencyResolver))
  }

  reactions += {
    case _ => updateGui()
  }

  def updateGui(): Unit = {
    if (gameStarted) {
      println("Update GUI...")
      displayGameScene()
    }
  }

}
