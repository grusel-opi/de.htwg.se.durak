package de.htwg.se.durak.util

import de.htwg.se.durak.model.{Card, CardColor, CardValue}
import scalafx.scene.image.{Image, ImageView}

object CardImgConverter {

  def getCardColorPath(card: Card):String = {
    card.color match {
      case CardColor.Herz => "cards/heart_"
      case CardColor.Pik => "cards/spade_"
      case CardColor.Kreuz => "cards/club_"
      case CardColor.Karo => "cards/diamond_"
    }
  }

  def getCardValuePath(card: Card): String = {
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

  def convertCardToImgView(card: Card): ImageView = {
    val cardColorPath: String = getCardColorPath(card)
    val cardValuePath: String = getCardValuePath(card)
    val completeImgPath: String = cardColorPath + cardValuePath

    return new ImageView(new Image(completeImgPath))
  }
}
