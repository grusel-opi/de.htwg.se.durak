package de.htwg.se.durak.util.cardConverter

import de.htwg.se.durak.model.cardComponent.CardInterface
import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{CardColor, CardValue}
import scalafx.scene.image.{Image, ImageView}

object CardImgConverter {

  def getCardColorPath(card: CardInterface):String = {
    card.color match {
      case CardColor.Herz => "cards/heart_"
      case CardColor.Pik => "cards/spade_"
      case CardColor.Kreuz => "cards/club_"
      case CardColor.Karo => "cards/diamond_"
    }
  }

  def getCardValuePath(card: CardInterface): String = {
    card.value match {
      case CardValue.Zwei => "2.png"
      case CardValue.Drei => "3.png"
      case CardValue.Vier => "4.png"
      case CardValue.Fünf => "5.png"
      case CardValue.Sechs => "6.png"
      case CardValue.Sieben => "7.png"
      case CardValue.Acht => "8.png"
      case CardValue.Neun => "9.png"
      case CardValue.Bube => "jack.png"
      case CardValue.Dame => "queen.png"
      case CardValue.König => "king.png"
      case CardValue.Zehn => "10.png"
      case CardValue.Ass => "1.png"
    }
  }

  def convertCardToImgView(card: CardInterface): ImageView = {
    new ImageView(new Image(getCardColorPath(card) + getCardValuePath(card)))
  }
}
