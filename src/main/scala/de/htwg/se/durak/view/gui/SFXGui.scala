package de.htwg.se.durak.view.gui

import java.net.URL
import java.util.ResourceBundle

import de.htwg.se.durak.Durak.getClass
import de.htwg.se.durak.controller.Controller
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.layout.{BorderPane, HBox}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.{FXMLView, NoDependencyResolver}
import scalafxml.core.macros.sfxml

import scala.swing.Reactor

object SFXGui extends JFXApp with Reactor {

  var controller: Controller = _

  def setController(controller: Controller): Unit = {
    this.controller = controller
    listenTo(controller)
  }

  val view = FXMLView(getClass.getResource("/gui.fxml"), NoDependencyResolver)

  val card = new Image("/cards/spade_1.png")
  val card1 = new Image("/cards/spade_2.png")
  val card2 = new Image("/cards/spade_3.png")
  val imgView = new ImageView(card)
  val imgView1 = new ImageView(card1)
  val imgView2 = new ImageView(card2)

  @FXML var rootPane: BorderPane = _
  @FXML var handCardBox: HBox = _
  @FXML var button: Button = _

  @FXML def schbebsi(): Unit = {
    if (handCardBox.getChildren.size() > 0) {
      handCardBox.getChildren.clear()
    } else {
      handCardBox.getChildren.addAll(imgView, imgView1, imgView2)
    }
  }

  stage = new PrimaryStage() {
    title = "FXML GridPane Demo"
    scene = new Scene(view)
  }

}
