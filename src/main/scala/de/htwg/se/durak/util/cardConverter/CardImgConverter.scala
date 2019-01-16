package de.htwg.se.durak.util.cardConverter

import de.htwg.se.durak.model.cardComponent.cardBaseImpl.{Card, CardColor, CardValue}
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

//  def getCardColorPath(cardColor: String): String = {
//    cardColor match {
//      case "Herz" => "cards/heart_"
//      case "Pik" => "cards/spade_"
//      case "Kreuz" => "cards/club_"
//      case "Karo" => "cards/diamond_"
//    }
//  }

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

//  def getCardValuePath(cardValue: String): String = {
//    cardValue match {
//      case "Zwei" => "2.png"
//      case "Drei" => "3.png"
//      case "Vier" => "4.png"
//      case "Fünf" => "5.png"
//      case "Sechs" => "6.png"
//      case "Sieben" => "7.png"
//      case "Acht" => "8.png"
//      case "Neun" => "9.png"
//      case "Bube" => "jack.png"
//      case "Dame" => "queen.png"
//      case "König" => "king.png"
//      case "Zehn" => "10.png"
//      case "Ass" => "1.png"
//    }
//  }

  def convertCardToImgView(card: Card): ImageView = {
    new ImageView(new Image(getCardColorPath(card) + getCardValuePath(card)))
  }

//  def convertCardToImgView(cardColor: String, cardValue: String): ImageView = {
//    new ImageView(new Image(getCardColorPath(cardColor) + getCardValuePath(cardValue)))
//  }
}
