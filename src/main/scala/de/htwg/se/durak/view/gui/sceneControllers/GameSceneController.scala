package de.htwg.se.durak.view.gui.sceneControllers

import de.htwg.se.durak.model.Card
import de.htwg.se.durak.util.CardImgConverter
import de.htwg.se.durak.view.gui.Gui
import scalafx.scene.layout._
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scalafx.scene.input.MouseEvent
import scalafx.Includes._
import scalafx.scene.control.{Button, ScrollPane}

import scalafx.scene.image.ImageView


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

  var selectedHandCard: Option[Card] = None
  var lastSelectedHandCard: Option[ImageView] = None
  var selectedCardToDefend: Option[Card] = None
  var lastSelectedCardToDefend: Option[ImageView] = None

  val CARD_HEIGHT = 100
  val CARD_WIDTH = 90

  update()

  def showTrumpCard(): Unit = {
    val trumpCardImgView = CardImgConverter.convertCardToImgView(Gui.controller.game.trump)
    trumpCardImgView.setFitHeight(CARD_HEIGHT)
    trumpCardImgView.setFitWidth(CARD_WIDTH)
    trumpCardBox.children.add(trumpCardImgView)
  }

  def showHandCards(): Unit = {
    val attacker = Gui.controller.game.currentTurn.attacker
    val neighbor = Gui.controller.game.currentTurn.neighbor
    val active = Gui.controller.game.active

    Gui.controller.game.active.handCards.foreach(card => {
      val cardImgView = CardImgConverter.convertCardToImgView(card)
      cardImgView.setFitHeight(CARD_HEIGHT)
      cardImgView.setFitWidth(CARD_WIDTH)
      setHandCardClickHandler(cardImgView, card)

      val stackPane: StackPane = new StackPane()
      stackPane.setMaxHeight(CARD_HEIGHT + 10)
      stackPane.setMinWidth(CARD_WIDTH + 10)
      stackPane.setStyle("-fx-background-color: transparent;")
      stackPane.children.add(cardImgView)

      handCardBox.children.add(stackPane)
    })
  }

  def setHandCardClickHandler(cardImgView: ImageView, card: Card): Unit = {
    val currentTurn = Gui.controller.game.currentTurn
    val attacker = Gui.controller.game.currentTurn.attacker
    val neighbor = Gui.controller.game.currentTurn.neighbor
    val active = Gui.controller.game.active

    cardImgView.onMouseClicked = (mouseEvent: MouseEvent) => {

      if (attacker.equals(active) || neighbor.equals(active)) {
        // attack
        Gui.controller.playCard(Some(card), None)
      } else {

        highlightSelectedHandCard(cardImgView, card)

        if (selectedHandCard != None && selectedCardToDefend != None) {
          // block
          Gui.controller.playCard(Some(card), selectedCardToDefend)
        }
      }
    }
  }

  def highlightSelectedHandCard(cardImgView: ImageView, card: Card): Unit = {
    val stackPane = cardImgView.getParent

    if (lastSelectedHandCard == Some(cardImgView)) {
      stackPane.setStyle("-fx-background-color: transparent;")
      lastSelectedHandCard = None
      selectedHandCard = None
    } else if(lastSelectedHandCard == None) {
      stackPane.setStyle("-fx-background-color: aquamarine; -fx-background-radius: 5;")
      lastSelectedHandCard = Some(cardImgView)
      selectedHandCard = Some(card)
    } else {
      lastSelectedHandCard.get.getParent().setStyle("-fx-background-color: transparent;")
      stackPane.setStyle("-fx-background-color: aquamarine; -fx-background-radius: 5;")
      lastSelectedHandCard = Some(cardImgView)
      selectedHandCard = Some(card)
    }
  }


  def showCardsToDefend(): Unit = {
    Gui.controller.game.currentTurn.attackCards.foreach(card => {
      val cardImgView = CardImgConverter.convertCardToImgView(card)
      cardImgView.setFitHeight(CARD_HEIGHT)
      cardImgView.setFitWidth(CARD_WIDTH)

      setCardToDefendClickHandler(cardImgView, card)

      val stackPane: StackPane = new StackPane()
      stackPane.setId("stackPane")
      stackPane.setMaxHeight(CARD_HEIGHT + 10)
      stackPane.setMinWidth(CARD_WIDTH + 10)
      stackPane.setStyle("-fx-background-color: transparent;")
      stackPane.children.add(cardImgView)

      cardsOnTableBox.children.add(stackPane)
    })

  }

  def setCardToDefendClickHandler(cardImgView: ImageView, card: Card): Unit = {
    cardImgView.onMouseClicked = (mouseEvent: MouseEvent) => {

      highlightSelectedCardToDefend(cardImgView, card)

      if (selectedHandCard != None && selectedCardToDefend != None) {
        // block
        Gui.controller.playCard(selectedHandCard, Some(card))
      }
    }
  }

  def highlightSelectedCardToDefend(cardImgView: ImageView, card: Card): Unit = {
    val victim = Gui.controller.game.currentTurn.victim
    val active = Gui.controller.game.active

    val stackPane = cardImgView.getParent

    // highlight cards on table only if the victim clicks on it.
    if (victim.equals(active)) {
      if (lastSelectedCardToDefend == Some(cardImgView)) {
        stackPane.setStyle("-fx-background-color: transparent;")
        lastSelectedCardToDefend = None
        selectedCardToDefend = None
      } else if(lastSelectedCardToDefend == None) {
        stackPane.setStyle("-fx-background-color: aquamarine; -fx-background-radius: 5;")
        lastSelectedCardToDefend = Some(cardImgView)
        selectedCardToDefend = Some(card)
      } else {
        lastSelectedCardToDefend.get.getParent().setStyle("-fx-background-color: transparent;")
        stackPane.setStyle("-fx-background-color: aquamarine; -fx-background-radius: 5;")
        lastSelectedCardToDefend = Some(cardImgView)
        selectedCardToDefend = Some(card)
      }
    } else {
      selectedCardToDefend = Some(card)
    }

  }

  def showDefendedCards(): Unit = {
    val blockedBy = Gui.controller.game.currentTurn.blockedBy

    blockedBy.foreach(card => {
      val attackCardImgView = CardImgConverter.convertCardToImgView(card._1)
      val blockingCardImgView = CardImgConverter.convertCardToImgView(card._2)

      attackCardImgView.setFitHeight(CARD_HEIGHT)
      attackCardImgView.setFitWidth(CARD_WIDTH)
      blockingCardImgView.setFitHeight(CARD_HEIGHT)
      blockingCardImgView.setFitWidth(CARD_WIDTH)

      val cardVBox: VBox = new VBox()
      cardVBox.spacing = -CARD_WIDTH / 2

      cardVBox.children.addAll(attackCardImgView, blockingCardImgView)

      cardsOnTableBox.children.add(cardVBox)
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
    neighborTextContent.setText(Gui.controller.game.currentTurn.neighbor.name)
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

  def configureOkayButton(): Unit = {
    val attacker = Gui.controller.game.currentTurn.attacker
    val victim = Gui.controller.game.currentTurn.victim
    val active = Gui.controller.game.active

    val attackCards = Gui.controller.game.currentTurn.attackCards
    val blockedBy = Gui.controller.game.currentTurn.blockedBy

    if (active.equals(attacker) && attackCards.isEmpty && blockedBy.isEmpty) {
      okayButton.setDisable(true)
    } else if (active.equals(victim)) {
      okayButton.setDisable(true)
      okayButton.setVisible(false)
    } else {
      okayButton.setDisable(false)
      okayButton.setVisible(true)
    }
  }

  def configureTakeButton(): Unit = {
    val victim = Gui.controller.game.currentTurn.victim
    val active = Gui.controller.game.active

    if (!active.equals(victim)) {
      takeButton.setDisable(true)
      takeButton.setVisible(false)
    } else {
      takeButton.setDisable(false)
      takeButton.setVisible(true)
    }
  }

  def update(): Unit = {
    setGameSeceneStatusText()
    configureOkayButton()
    configureTakeButton()
    showTrumpCard()
    showHandCards()
    showCardsToDefend()
    showDefendedCards()
    configureHandCardScrollPane()
    configureCardsOnTableScrollPane()
  }

}
