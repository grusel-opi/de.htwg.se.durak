package de.htwg.se.durak.view.gui

import java.io.File

import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.controller.events.Notification
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javax.sound.sampled.{AudioInputStream, AudioSystem}
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
    case notification: Notification => notifyUser(notification)
    case _ => updateGui()

  }

  def notifyUser(notification: Notification): Unit = {

    notification.getMessage() match {
      case "Player name already present!" => {
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + notification.getMessage()
          contentText = "Please enter a different player name."
        }.showAndWait()
      }

      case "There are no cards to take." => {
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + notification.getMessage()
          contentText = ""
        }.showAndWait()
      }
    }
  }

  def updateGui(): Unit = {
    if (gameStarted) {
      println("Update GUI...")
      displayGameScene()
    }
  }

}
