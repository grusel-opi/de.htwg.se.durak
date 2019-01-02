package de.htwg.se.durak.view.gui.scenes

import java.beans.EventHandler

import de.htwg.se.durak.util.CardImgConverter
import de.htwg.se.durak.view.gui.SFXGui
import scalafx.scene.layout.HBox
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.scene.input.MouseEvent

import scala.swing.Button

@sfxml
class GameScene( private val attackerPlayerText: Text,
                 private val victimPlayerText: Text,
                 private val activePlayerText: Text,
                 private val trumpCardBox: HBox,
                 private val handCardBox: HBox,
                 private val attackCardBox: HBox,
                 private val okayButton: Button,
                 private val undoButton: Button) {

  attackerPlayerText.setText(SFXGui.controller.game.currentTurn.attacker.name)
  victimPlayerText.setText(SFXGui.controller.game.currentTurn.victim.name)
  activePlayerText.setText(SFXGui.controller.game.active.name)

  val trumpCardImgView = CardImgConverter.convertCardToImgView(SFXGui.controller.game.trump)

  trumpCardImgView.setFitHeight(100)
  trumpCardImgView.setFitWidth(90)
  trumpCardBox.children.add(trumpCardImgView)

  val handCards = SFXGui.controller.game.active.handCards

  handCards.foreach(card => {
    val cardImgView = CardImgConverter.convertCardToImgView(card)
    cardImgView.setFitHeight(100)
    cardImgView.setFitWidth(90)
    cardImgView.onMouseClicked = (me: MouseEvent) => {
      SFXGui.controller.playCard(Option(card), None)
      attackCardBox.children.add(cardImgView)
      handCardBox.children.remove(cardImgView)
    }
    handCardBox.children.add(cardImgView)
  })

  def okayButtonPressed(): Unit = {
    // TODO player should only be okay, if cards were played!
    SFXGui.controller.playOK()
    SFXGui.displayGameScene()
  }

  def test(): Unit = {
    println("clicked h box!")
  }
}
