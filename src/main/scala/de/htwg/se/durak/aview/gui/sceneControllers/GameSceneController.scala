package de.htwg.se.durak.aview.gui.sceneControllers

import de.htwg.se.durak.aview.gui.Gui
import scalafx.scene.layout._
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scalafx.scene.input.MouseEvent
import scalafx.Includes._
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.image.ImageView

import de.htwg.se.durak.Durak
import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.util.cardConverter.{CardImgConverter, CardStringConverter}


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
                          private val attackCardsBox: HBox,
                          private val attackCardsScrollPane: ScrollPane,
                          private val defendedCardsBox: HBox,
                          private val defendedCardsScrollPane: ScrollPane,
                          private val takeButton: Button,
                          private val okayButton: Button,
                          private val undoButton: Button,
                          private val mainMenuButton: Button) {

  val gui: Gui = Durak.gui
  var selectedHandCard: Option[Card] = None
  var lastSelectedHandCard: Option[ImageView] = None
  var selectedCardToDefend: Option[Card] = None
  var lastSelectedCardToDefend: Option[ImageView] = None

  val CARD_HEIGHT = 100
  val CARD_WIDTH = 90

  update()

  def showTrumpCard(): Unit = {
    val trumpCardAsString: String = gui.controller.trumpCardToString()
    val card = CardStringConverter.parseCardStringToCardObject(trumpCardAsString)

    val trumpCardImgView = CardImgConverter.convertCardToImgView(card)
    trumpCardImgView.setFitHeight(CARD_HEIGHT)
    trumpCardImgView.setFitWidth(CARD_WIDTH)
    trumpCardBox.children.add(trumpCardImgView)
  }

  def showHandCards(): Unit = {
    val activeHandCardsAsString: String = gui.controller.activePlayerHandCardsToString()
    val activeHandCardsArray: Array[String] = activeHandCardsAsString.split(",")

    activeHandCardsArray.foreach(cardString => {
      val card = CardStringConverter.parseCardStringToCardObject(cardString)
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
    val attackerAsString: String = gui.controller.currentAttackerToString()
    val neighbourAsString: String = gui.controller.currentNeighbourToString()
    val activeAsString: String = gui.controller.activePlayerToString()

    cardImgView.onMouseClicked = (_: MouseEvent) => {
      if (attackerAsString.equals(activeAsString) || neighbourAsString.equals(activeAsString)) {
        // attack
        gui.controller.playCard(Some(card), None)
      } else {

        highlightSelectedHandCard(cardImgView, card)

        if (selectedHandCard.isDefined && selectedCardToDefend.isDefined) {
          // block
          gui.controller.playCard(Some(card), selectedCardToDefend)
        }
      }
    }
  }

  def highlightSelectedHandCard(cardImgView: ImageView, card: Card): Unit = {
    val stackPane = cardImgView.getParent

    if (lastSelectedHandCard.contains(cardImgView)) {
      stackPane.setStyle("-fx-background-color: transparent;")
      lastSelectedHandCard = None
      selectedHandCard = None
    } else if (lastSelectedHandCard.isEmpty) {
      stackPane.setStyle("-fx-background-color: aquamarine; -fx-background-radius: 5;")
      lastSelectedHandCard = Some(cardImgView)
      selectedHandCard = Some(card)
    } else {
      lastSelectedHandCard.get.getParent.setStyle("-fx-background-color: transparent;")
      stackPane.setStyle("-fx-background-color: aquamarine; -fx-background-radius: 5;")
      lastSelectedHandCard = Some(cardImgView)
      selectedHandCard = Some(card)
    }
  }


  def showAttackCards(): Unit = {
    val attackCardsArray: Array[String] = gui.controller.currentAttackCardsToString().split(",")
    val blockedBy = gui.controller.getCurrentBlockedByMap()
    val activeAsString: String = gui.controller.activePlayerToString()
    val victimAsString: String = gui.controller.currentVictimToString()

    attackCardsArray.foreach(cardString => {
      if (!cardString.equals("")) {
        val card = CardStringConverter.parseCardStringToCardObject(cardString)
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

        attackCardsBox.children.add(stackPane)
      }
    })

    if (attackCardsArray.length > 0 && activeAsString.equals(victimAsString) && blockedBy.isEmpty) {
      val stackPaneToAddThrowableCard: StackPane = new StackPane()
      stackPaneToAddThrowableCard.setMaxHeight(CARD_HEIGHT + 10)
      stackPaneToAddThrowableCard.setMinWidth(CARD_WIDTH + 10)
      setStackPaneToAddThrowableCardClickHandler(stackPaneToAddThrowableCard)
      stackPaneToAddThrowableCard.setStyle("-fx-background-color: aquamarine; -fx-background-radius: 5;")
      val X: Double = 10
      val Y: Double = 50
      val throwInText = new Text(X, Y, "Throw in")
      stackPaneToAddThrowableCard.children.add(throwInText)

      attackCardsBox.children.add(attackCardsArray.length, stackPaneToAddThrowableCard)
    }
  }

  def setCardToDefendClickHandler(cardImgView: ImageView, card: Card): Unit = {
    cardImgView.onMouseClicked = (_: MouseEvent) => {

      highlightSelectedCardToDefend(cardImgView, card)

      if (selectedHandCard.isDefined && selectedCardToDefend.isDefined) {
        // block
        gui.controller.playCard(selectedHandCard, Some(card))
      }
    }
  }

  def setStackPaneToAddThrowableCardClickHandler(stackPane: StackPane): Unit = {
    stackPane.onMouseClicked = (_: MouseEvent) => {
      if (selectedHandCard.isDefined) {
        gui.controller.throwCardIn(selectedHandCard.get)
        okayButton.setDisable(false)
      }
    }
  }

  def highlightSelectedCardToDefend(cardImgView: ImageView, card: Card): Unit = {
    val victimAsString: String = gui.controller.currentVictimToString()
    val activeAsString: String = gui.controller.activePlayerToString()

    val stackPane = cardImgView.getParent

    // highlight cards on table only if the victim clicks on it.
    if (victimAsString.equals(activeAsString)) {
      if (lastSelectedCardToDefend.contains(cardImgView)) {
        stackPane.setStyle("-fx-background-color: transparent;")
        lastSelectedCardToDefend = None
        selectedCardToDefend = None
      } else if (lastSelectedCardToDefend.isEmpty) {
        stackPane.setStyle("-fx-background-color: aquamarine; -fx-background-radius: 5;")
        lastSelectedCardToDefend = Some(cardImgView)
        selectedCardToDefend = Some(card)
      } else {
        lastSelectedCardToDefend.get.getParent.setStyle("-fx-background-color: transparent;")
        stackPane.setStyle("-fx-background-color: aquamarine; -fx-background-radius: 5;")
        lastSelectedCardToDefend = Some(cardImgView)
        selectedCardToDefend = Some(card)
      }
    } else {
      selectedCardToDefend = Some(card)
    }

  }

  def showDefendedCards(): Unit = {
    val blockedBy = gui.controller.getCurrentBlockedByMap()

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

      defendedCardsBox.children.add(cardVBox)
    })

  }

  def takeButtonPressed(): Unit = {
    gui.controller.takeCards()
  }

  def okayButtonPressed(): Unit = {
    gui.controller.playOk()

  }

  def undoButtonPressed(): Unit = {
    gui.controller.undo()
  }

  def setGameSeceneStatusText(): Unit = {
    attackerPlayerText.setText(gui.controller.currentAttackerToString())
    victimPlayerText.setText(gui.controller.currentVictimToString())
    activePlayerText.setText(gui.controller.activePlayerToString())
    cardsInDeckText.setText(gui.controller.deckSizeToString())
    neighborTextContent.setText(gui.controller.currentNeighbourToString())
  }

  def configureHandCardScrollPane(): Unit = {
    // disable verical scrolling
    handCardScrollPane.setFitToHeight(true)
    // remove ugly border around ScrollPane
    handCardScrollPane.getStyleClass.add("edge-to-edge")
  }

  def configureAttackCardsScrollPane(): Unit = {
    // disable verical scrolling
    attackCardsScrollPane.setFitToHeight(true)
    // remove ugly border around ScrollPane
    attackCardsScrollPane.getStyleClass.add("edge-to-edge")
  }

  def configureDefendedCardsScrollPane(): Unit = {
    // disable verical scrolling
    attackCardsScrollPane.setFitToHeight(true)
    // remove ugly border around ScrollPane
    attackCardsScrollPane.getStyleClass.add("edge-to-edge")
  }

  def configureOkayButton(): Unit = {
    val attackerAsString: String = gui.controller.currentAttackerToString()
    val victimAsString: String = gui.controller.currentVictimToString()
    val neighbourAsString: String = gui.controller.currentNeighbourToString()
    val activeAsString: String = gui.controller.activePlayerToString()
    val attackCardsArray: Array[String] = gui.controller.currentAttackCardsToString().split(",")
    val blockedBy = gui.controller.getCurrentBlockedByMap()

    //    if (!attackCardsArray(0).equals("")) {
    //      println("1")
    //      if (activeAsString.equals(attackerAsString) || activeAsString.equals(neighbourAsString)) {
    //        okayButton.setDisable(false)
    //        okayButton.setVisible(true)
    //      } else if (activeAsString.equals(victimAsString)) {
    //        okayButton.setVisible(false)
    //      }
    //    } else {
    //      println("2")
    //      if (activeAsString.equals(attackerAsString) || activeAsString.equals(neighbourAsString)) {
    //        if (blockedBy.nonEmpty) {
    //          okayButton.setDisable(false)
    //          okayButton.setVisible(true)
    //        } else {
    //        }
    //      }
    //    }
    //
    //    if (activeAsString.equals(neighbourAsString) && !activeAsString.equals(attackerAsString)) {
    //      okayButton.setDisable(false)
    //      okayButton.setVisible(true)
    //    }

    if (activeAsString.equals(neighbourAsString) || activeAsString.equals(attackerAsString)) {
      okayButton.setDisable(false)
      okayButton.setVisible(true)
    }
  }

    def configureTakeButton(): Unit = {
      val victimAsString: String = gui.controller.currentVictimToString()
      val activeAsString: String = gui.controller.activePlayerToString()

      if (!activeAsString.equals(victimAsString)) {
        takeButton.setDisable(true)
        takeButton.setVisible(false)
      } else {
        takeButton.setDisable(false)
        takeButton.setVisible(true)
      }
    }

    def mainMenuButtonPressed(): Unit = {
      val confirmButton: ButtonType = new ButtonType("Ok", ButtonData.Apply)
      val abortButton: ButtonType = new ButtonType("No", ButtonData.No)

      val res: Option[String] = new TextInputDialog("") {
        title = "Save Game"
        headerText = "Do you want to save the game before returning to Main Menu?"
        contentText = "If so, enter a file name:"
      }.showAndWait()

      if (res.isDefined) {
        gui.controller.saveGame(res.get)
      }

      gui.controller.resetPlayers()
      gui.displayMainMenuScene()


      // }
    }

    def update(): Unit = {
      setGameSeceneStatusText()
      configureOkayButton()
      configureTakeButton()
      showTrumpCard()
      showHandCards()
      showDefendedCards()
      showAttackCards()
      configureHandCardScrollPane()
      configureAttackCardsScrollPane()
      configureDefendedCardsScrollPane()
    }

  }
