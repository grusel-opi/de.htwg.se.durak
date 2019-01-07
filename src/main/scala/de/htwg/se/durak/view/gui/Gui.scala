package de.htwg.se.durak.view.gui

import de.htwg.se.durak.controller.Controller
import de.htwg.se.durak.controller.events.{Notification, GameOverEvent}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafxml.core.{FXMLView, NoDependencyResolver}
import scala.swing.Reactor

object Gui extends JFXApp with Reactor {

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
    stage.scene = new Scene(FXMLView(getClass.getResource("/NewGameScene.fxml"), NoDependencyResolver))
  }

  def displayGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/gameScene.fxml"), NoDependencyResolver))
  }

  def displayWinningGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/winningGameScene.fxml"), NoDependencyResolver))
  }

  def displayMainMenuScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/mainMenuScene.fxml"), NoDependencyResolver))
  }

  def exitGame(): Unit = {
    System.exit(0)
  }

  def displayRulesScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/rulesScene.fxml"), NoDependencyResolver))
  }

  reactions += {
    case n: Notification => notifyUser(n)
    case goe: GameOverEvent => {
      gameStarted = false
      displayWinningGameScene()
    }
    case _ => updateGui()

  }

  def notifyUser(notification: Notification): Unit = {
    notification.getMessage() match {
      case "Player name already present!" =>
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + notification.getMessage()
          contentText = "Please enter a different player name."
        }.showAndWait()

      case "There are no cards to take." =>
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + notification.getMessage()
          contentText = ""
        }.showAndWait()

      case "Illegal Turn!" => {
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + notification.getMessage()
          contentText = ""
        }.showAndWait()
        updateGui()
      }

      case "Missing blocking Card!" => {
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + notification.getMessage()
          contentText = "You have to specify a blocking card!"
        }.showAndWait()
      }

      case _ =>
    }
  }

  def updateGui(): Unit = {
    if (gameStarted) {
      println("update gui")
      displayGameScene()
    }
  }

}
