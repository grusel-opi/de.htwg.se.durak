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

  val resource = getClass.getResource("/gui.fxml")
  if (resource == null) {
    throw new IOException("resource not found!")
  }
  val view = FXMLView(resource, NoDependencyResolver)

  stage = new PrimaryStage() {
    title = "FXML Demo"
    scene = new Scene(view)
  }

}
