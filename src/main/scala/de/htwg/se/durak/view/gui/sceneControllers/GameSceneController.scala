package de.htwg.se.durak.view.gui.sceneControllers

import de.htwg.se.durak.model.Card
import de.htwg.se.durak.util.CardImgConverter
import de.htwg.se.durak.view.gui.Gui
import scalafx.scene.layout.{AnchorPane, HBox, VBox}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scalafx.scene.input.MouseEvent
import scalafx.Includes._
import scalafx.scene.control.{Alert, Button, ScrollPane}
import scalafx.scene.control.Alert.AlertType


@sfxml
class GameSceneController(private val rootPane: AnchorPane,
                          private val attackerPlayerText: Text,
                          private val victimPlayerText: Text,
                          private val activePlayerText: Text,
                          private val neighborText: Text,
                          private val neighborTextContent: Text,
                          private val cardsInDeckText: Text,
                          private val trumpCardBox: HBox,
                          private val handCardBox: HBox,
                          private val handCardScrollPane: ScrollPane,
                          private val cardsOnTableBox: HBox,
                          private val cardsOnTableScrollPane: ScrollPane,
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
  checkIfPlayerHasFinished()
  configureHandCardScrollPane()
  configureCardsOnTableScrollPane()

  def hideOkayButtonForVictim(): Unit = {
    if (Gui.controller.game.active.equals(Gui.controller.game.currentTurn.victim)) {
      okayButton.setDisable(true)
      okayButton.setVisible(false)
    } else {
      okayButton.setDisable(false)
      okayButton.setVisible(true)
    }
  }

  def showTrumpCard(): Unit = {
    val trumpCardImgView = CardImgConverter.convertCardToImgView(Gui.controller.game.trump)
    trumpCardImgView.setFitHeight(CARD_HEIGHT)
    trumpCardImgView.setFitWidth(CARD_WIDTH)
    trumpCardBox.children.add(trumpCardImgView)
  }

  def showHandCards(): Unit = {
    val attacker = Gui.controller.game.currentTurn.attacker
    val active = Gui.controller.game.active

    Gui.controller.game.active.handCards.foreach(card => {
      val cardImgView = CardImgConverter.convertCardToImgView(card)
      cardImgView.setFitHeight(CARD_HEIGHT)
      cardImgView.setFitWidth(CARD_WIDTH)
      cardImgView.onMouseClicked = (me: MouseEvent) => {
        if (attacker.equals(active)) {
          Gui.controller.playCard(Some(card), None)
        } else {
          if (cardToBlock.nonEmpty) {
            Gui.controller.playCard(Some(card), cardToBlock)
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
    Gui.controller.game.currentTurn.attackCards.foreach(card => {
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
    Gui.controller.game.currentTurn.blockedBy.foreach(card => {
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
    Gui.controller.takeCards()
  }

  def okayButtonPressed(): Unit = {
    Gui.controller.playOK()
  }

  def undoButtonPressed(): Unit = {
    Gui.controller.undo()
  }

  def setGameSeceneStatusText(): Unit = {
    attackerPlayerText.setText(Gui.controller.game.currentTurn.attacker.name)
    victimPlayerText.setText(Gui.controller.game.currentTurn.victim.name)
    activePlayerText.setText(Gui.controller.game.active.name)
    cardsInDeckText.setText(Gui.controller.game.deck.cards.size.toString)

    if (Gui.controller.players.size > 2) {
      neighborTextContent.setText(Gui.controller.game.currentTurn.neighbor.name)
    } else {
      neighborText.setVisible(false)
      neighborTextContent.setVisible(false)
    }
  }

  // TODO: Add winningPlayer to Durak Game
  def checkIfPlayerHasFinished(): Unit = {
    if (Gui.controller.game.active.handCards.isEmpty) {
      Gui.controller.game.win
    }
  }

  def configureHandCardScrollPane(): Unit = {
    // disable verical scrolling
    handCardScrollPane.setFitToHeight(true)
    // remove ugly border around ScrollPane
    handCardScrollPane.getStyleClass.add("edge-to-edge")
  }

  def configureCardsOnTableScrollPane(): Unit = {
    // disable verical scrolling
    cardsOnTableScrollPane.setFitToHeight(true);
    // remove ugly border around ScrollPane
    cardsOnTableScrollPane.getStyleClass.add("edge-to-edge")
  }

}
