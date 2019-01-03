package de.htwg.se.durak.view.gui.scenes

import de.htwg.se.durak.model.Card
import de.htwg.se.durak.util.CardImgConverter
import de.htwg.se.durak.view.gui.SFXGui
import scalafx.scene.layout.{AnchorPane, Background, HBox, VBox}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scalafx.scene.input.MouseEvent
import scalafx.Includes._
import scalafx.scene.control.{Alert, Button, ScrollPane}
import scalafx.scene.control.Alert.AlertType


@sfxml
class GameScene(private val rootPane: AnchorPane,
                private val attackerPlayerText: Text,
                private val victimPlayerText: Text,
                private val activePlayerText: Text,
                private val neighborOneText: Text,
                private val neighborOneTextContent: Text,
                private val trumpCardBox: HBox,
                private val handCardBox: HBox,
                private val handCardScrollPane: ScrollPane,
                private val cardsOnTableBox: HBox,
                private val cardsOnTabeScrollPane: ScrollPane,
                private val takeButton: Button,
                private val okayButton: Button,
                private val undoButton: Button) {

  var cardToBlock: Option[Card] = None
  val CARD_HEIGHT = 100
  val CARD_WIDTH = 90

  setGameSeceneStatusText()
  hideOkayButtonForVictim()
  showTrumpCard()
  showHandCards()
  showAttackCards()
  showBlockingCards()

  def hideOkayButtonForVictim(): Unit = {
    if (SFXGui.controller.game.active.equals(SFXGui.controller.game.currentTurn.victim)) {
      okayButton.setDisable(true)
      okayButton.setVisible(false)
    } else {
      okayButton.setDisable(false)
      okayButton.setVisible(true)
    }
  }

  def showTrumpCard(): Unit = {
    val trumpCardImgView = CardImgConverter.convertCardToImgView(SFXGui.controller.game.trump)
    trumpCardImgView.setFitHeight(CARD_HEIGHT)
    trumpCardImgView.setFitWidth(CARD_WIDTH)
    trumpCardBox.children.add(trumpCardImgView)
  }

  def showHandCards(): Unit = {
    val attacker = SFXGui.controller.game.currentTurn.attacker
    val active = SFXGui.controller.game.active

    SFXGui.controller.game.active.handCards.foreach(card => {
      val cardImgView = CardImgConverter.convertCardToImgView(card)
      cardImgView.setFitHeight(CARD_HEIGHT)
      cardImgView.setFitWidth(CARD_WIDTH)
      cardImgView.onMouseClicked = (me: MouseEvent) => {
        if (attacker.equals(active)) {
          println("clicked on: " + card)
          SFXGui.controller.playCard(Some(card), None)
        } else {
          if (cardToBlock.nonEmpty) {
            SFXGui.controller.playCard(Some(card), cardToBlock)
          } else {
            new Alert(AlertType.Information) {
              title = "Information Dialog"
              headerText = "Information: No card to block was selected."
              contentText = "Please first choose a card you want to block."
            }.showAndWait()
          }
        }
      }
      handCardBox.children.add(cardImgView)
    })
  }

  def showAttackCards(): Unit = {
    SFXGui.controller.game.currentTurn.attackCards.foreach(card => {
      val cardImgView = CardImgConverter.convertCardToImgView(card)
      cardImgView.setFitHeight(CARD_HEIGHT)
      cardImgView.setFitWidth(CARD_WIDTH)
      cardImgView.onMouseClicked = (me: MouseEvent) => {
        cardToBlock = Some(card)
      }
      cardsOnTableBox.children.add(cardImgView)
    })
  }


  def showBlockingCards(): Unit = {
    SFXGui.controller.game.currentTurn.blockedBy.foreach(card => {
      val attackCardImgView = CardImgConverter.convertCardToImgView(card._1)
      val blockingCardImgView = CardImgConverter.convertCardToImgView(card._2)

      attackCardImgView.setFitHeight(CARD_HEIGHT)
      attackCardImgView.setFitWidth(CARD_WIDTH)
      blockingCardImgView.setFitHeight(CARD_HEIGHT)
      blockingCardImgView.setFitWidth(CARD_WIDTH)

      val attackCardVBox: VBox = new VBox()
      attackCardVBox.spacing = -CARD_WIDTH / 2

      attackCardVBox.children.addAll(attackCardImgView, blockingCardImgView)
      cardsOnTableBox.children.add(attackCardVBox)
    })
  }

  def takeButtonPressed(): Unit = {
    SFXGui.controller.takeCards()
  }

  def okayButtonPressed(): Unit = {
    // TODO player should only be okay, if cards were played!
    SFXGui.controller.playOK()
  }

  def undoButtonPressed(): Unit = {
    SFXGui.controller.undo()
  }

  def setGameSeceneStatusText(): Unit = {
    attackerPlayerText.setText(SFXGui.controller.game.currentTurn.attacker.name)
    victimPlayerText.setText(SFXGui.controller.game.currentTurn.victim.name)
    activePlayerText.setText(SFXGui.controller.game.active.name)
    if (SFXGui.controller.players.size > 2) {
      neighborOneTextContent.setText(SFXGui.controller.game.currentTurn.neighbor.name)
    } else {
      neighborOneText.setVisible(false)
      neighborOneTextContent.setVisible(false)
    }
  }
}
