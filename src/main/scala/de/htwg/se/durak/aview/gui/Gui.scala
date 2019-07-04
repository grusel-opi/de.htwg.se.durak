package de.htwg.se.durak.aview.gui

import de.htwg.se.durak.controller.controllerComponent._
import de.htwg.se.durak.util.customExceptions._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.WindowEvent
import scalafxml.core.{FXMLView, NoDependencyResolver}
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

  def displayLoadGameScene(): Unit = {
    stage.scene = new Scene(FXMLView(getClass.getResource("/scenes/loadGameScene.fxml"), NoDependencyResolver))
  }

  reactions += {
    case eE: ExceptionEvent => notifyUser(eE)
    case _: ResetPlayersEvent => displayNewGameScene()
    case _: NewGameEvent => displayGameScene()
    case _: CardsChangedEvent => displayGameScene()
    case _: GameOverEvent => displayWinningGameScene()
    case _: NewPlayerEvent => displayNewGameScene()
  }

  def displayIllegalTurnException(eE: ExceptionEvent): Unit = {
    new Alert(AlertType.Warning) {
      title = "Warning Dialog"
      headerText = "Warning: " + eE.getException.getMessage
      contentText = ""
    }.showAndWait
  }

  def displayNoCardsToTakeException(eE: ExceptionEvent): Unit = {
    new Alert(AlertType.Warning) {
      title = "Warning Dialog"
      headerText = "Warning: " + eE.getException.getMessage
      contentText = ""
    }.showAndWait
  }

  def displayPlayerAlreadyPresentException(eE: ExceptionEvent): Unit = {
    new Alert(AlertType.Warning) {
      title = "Warning Dialog"
      headerText = "Warning: " + eE.getException.getMessage
      contentText = "Please enter a different player name."
    }.showAndWait
  }

  def displayMissingBlockingCardException(eE: ExceptionEvent): Unit = {
    new Alert(AlertType.Warning) {
      title = "Warning Dialog"
      headerText = "Warning: " + eE.getException.getMessage
      contentText = "You have to specify a blocking card!"
    }.showAndWait
  }

  def displayMoreThanOnePlayerNeededException(eE: ExceptionEvent): Unit = {
    new Alert(AlertType.Warning) {
      title = "Warning Dialog"
      headerText = "Warning: " + eE.getException.getMessage
      contentText = "Please add another player first."
    }.showAndWait
  }

  def displayVictimHasNotEnoughCardsToBlockException(eE: ExceptionEvent): Unit = {
    new Alert(AlertType.Warning) {
      title = "Warning Dialog"
      headerText = "Warning: " + eE.getException.getMessage
      contentText = ""
    }.showAndWait
  }

  def displayLayCardFirsException(eE: ExceptionEvent): Unit = {
    new Alert(AlertType.Warning) {
      title = "Warning Dialog"
      headerText = "Warning: " + eE.getException.getMessage
      contentText = ""
    }.showAndWait()
  }

  def notifyUser(eE: ExceptionEvent): Unit = {
    eE.getException match {
      case _: IllegalTurnException =>
        displayIllegalTurnException(eE)

      case _: NoCardsToTakeException =>
        displayNoCardsToTakeException(eE)

      case _: PlayerAlreadyPresentException =>
        displayPlayerAlreadyPresentException(eE)

      case _: MissingBlockingCardException =>
        displayMissingBlockingCardException(eE)

      case _: MoreThanOnePlayerNeededException =>
        displayMoreThanOnePlayerNeededException(eE)

      case _: VictimHasNotEnoughCardsToBlockException =>
        displayVictimHasNotEnoughCardsToBlockException(eE)

      case _: LayCardFirsException =>
        displayLayCardFirsException(eE)
    }
  }

  stage.onCloseRequest = (_: WindowEvent) => controller.exitGame()

}
