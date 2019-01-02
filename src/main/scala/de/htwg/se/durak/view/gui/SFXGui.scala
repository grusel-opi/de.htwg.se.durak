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


  var controller: Controller = _

  def setController(controller: Controller): Unit = {
    this.controller = controller
    listenTo(controller)
  }

  var resource = getClass.getResource("/gui.fxml")
  if (resource == null) {
    throw new IOException("resource not found!")
  }

  stage = new PrimaryStage() {
    title = "Durak"
    scene = new Scene(FXMLView(resource, NoDependencyResolver))
  }

  def displayNewGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/newGame.fxml"), NoDependencyResolver))
  }

  def displayGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/game.fxml"), NoDependencyResolver))
  }

  reactions += {
    case _ => updateGui()
  }

  def updateGui(): Unit = {
    println("UPDATE!")
  }

}
