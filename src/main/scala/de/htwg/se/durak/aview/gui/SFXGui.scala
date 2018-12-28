package de.htwg.se.durak.aview.gui

import javafx.scene.layout.HBox
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLView, NoDependencyResolver}
import scalafx.Includes._
import scalafx.scene.image.{Image, ImageView}

object SFXGui extends JFXApp {

  val view = FXMLView(getClass.getResource("/gui.fxml"), NoDependencyResolver)
  val card = new Image("/cards/spade_1.png")
  val card1 = new Image("/cards/spade_2.png")
  val card2 = new Image("/cards/spade_3.png")


  val imgView = new ImageView(card)

  val imgView1 = new ImageView(card1)

  val imgView2 = new ImageView(card2)



  stage = new PrimaryStage() {
    title = "FXML GridPane Demo"
    scene = new Scene(view)
  }

  val box: HBox = stage.scene().lookup("#handCardBox").asInstanceOf[HBox]

  box.getChildren.addAll(imgView, imgView1, imgView2)

}
