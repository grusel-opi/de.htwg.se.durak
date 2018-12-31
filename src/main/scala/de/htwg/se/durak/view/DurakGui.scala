package de.htwg.se.durak.view

import javafx.scene.control.Button
import javafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml

@sfxml
class DurakGui(private val rootPane: BorderPane,
               private val handCardBox: HBox,
               private val button: Button) {


  val card = new Image("/cards/spade_1.png")
  val card1 = new Image("/cards/spade_2.png")
  val card2 = new Image("/cards/spade_3.png")
  val imgView = new ImageView(card)
  val imgView1 = new ImageView(card1)
  val imgView2 = new ImageView(card2)


  def schbebsi(): Unit = {
    if (handCardBox.getChildren.size() > 0) {
      handCardBox.getChildren.clear()
    } else {
      handCardBox.getChildren.addAll(imgView, imgView1, imgView2)
    }
  }

}
