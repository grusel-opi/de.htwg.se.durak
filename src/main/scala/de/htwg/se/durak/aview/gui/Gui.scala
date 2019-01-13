package de.htwg.se.durak.aview.gui

import de.htwg.se.durak.controller.controllerComponent._
import de.htwg.se.durak.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.durak.util.customExceptions._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafxml.core.{FXMLView, NoDependencyResolver}
import scala.io.Source

import scala.swing.Reactor

case class Gui(controller: ControllerInterface) extends JFXApp with Reactor {

  listenTo(controller)

  stage = new PrimaryStage() {
    title = "Durak"
    scene = new Scene(FXMLView(getClass.getResource("/scenes/mainMenuScene.fxml"), NoDependencyResolver))
  }

  def displayNewGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/scenes/NewGameScene.fxml"), NoDependencyResolver))
  }

  def displayGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/scenes/gameScene.fxml"), NoDependencyResolver))
  }

  def displayWinningGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/scenes/winningGameScene.fxml"), NoDependencyResolver))
  }

  def displayMainMenuScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/scenes/mainMenuScene.fxml"), NoDependencyResolver))
  }

  def exitGame(): Unit = {
    System.exit(0)
  }

  def displayRulesScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/scenes/rulesScene.fxml"), NoDependencyResolver))
  }

  def displayLoadGameScene() : Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/scenes/loadGameScene.fxml"), NoDependencyResolver))
  }

  reactions += {
    case eE: ExceptionEvent => notifyUser(eE)
    case _: ResetPlayersEvent => displayNewGameScene()
    case _: NewGameEvent => displayGameScene()
    case _: CardsChangedEvent => displayGameScene()
    case _: GameOverEvent => displayWinningGameScene()
    case _: NewPlayerEvent => displayNewGameScene()
    case _ =>
      System.err.println("Unhandled Event... - FIX ME :(")

  }

  def notifyUser(exceptionEvent: ExceptionEvent): Unit = {
    exceptionEvent.getException match {
      case _: IllegalTurnException =>
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + exceptionEvent.getException.getMessage
          contentText = ""
        }.showAndWait
        displayGameScene()

      case _: NoCardsToTakeException =>
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + exceptionEvent.getException.getMessage
          contentText = ""
        }.showAndWait

      case _: PlayerAlreadyPresentException =>
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + exceptionEvent.getException.getMessage
          contentText = "Please enter a different player name."
        }.showAndWait

      case _: MissingBlockingCardException =>
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + exceptionEvent.getException.getMessage
          contentText = "You have to specify a blocking card!"
        }.showAndWait

      case _: MoreThanOnePlayerNeededException =>
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + exceptionEvent.getException.getMessage
          contentText = "Please add another player first."
        }.showAndWait

      case _: VictimHasNotEnoughCardsToBlockException =>
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + exceptionEvent.getException.getMessage
          contentText = ""
        }.showAndWait

      case _: LayCardFirsException => {
        new Alert(AlertType.Warning) {
          title = "Warning Dialog"
          headerText = "Warning: " + exceptionEvent.getException.getMessage
          contentText = ""
        }.showAndWait()
      }
      case _ => System.err.println("Unhandled Exception - FIX ME :(")
    }
  }
}
