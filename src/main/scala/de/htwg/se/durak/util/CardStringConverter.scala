package de.htwg.se.durak.util

import de.htwg.se.durak.model.{CardColor, CardValue}
import de.htwg.se.durak.model.CardColor.CardColor
import de.htwg.se.durak.model.CardValue.CardValue

object CardStringConverter {
  //noinspection ScalaStyle
  val excluded = List(0x1F0BF, 0x1F0AC, 0x1F0BC, 0x1F0CC, 0x1F0CF, 0x1F0DC)
  val list: List[String] = (0x1F0A1 to 0x1F0DE).filterNot(i => excluded.contains(i)).map(Character.toChars(_).mkString).toList

  def parseColor(input: String): CardColor = input match {
    case "Herz" | "herz"  => CardColor.Hearts
    case "Karo" | "karo"  => CardColor.Diamond
    case "Kreuz"| "Kreuz" => CardColor.Clubs
    case "Pik"  | "Pik"   => CardColor.Spades
  }

  def parseValue(input: String): CardValue = input match {
    case "2" => CardValue.Two
    case "3" => CardValue.Three
    case "4" => CardValue.Four
    case "5" => CardValue.Five
    case "6" => CardValue.Six
    case "7" => CardValue.Seven
    case "8" => CardValue.Eight
    case "9" => CardValue.Nine
    case "B" => CardValue.Jack
    case "D" => CardValue.Queen
    case "K" => CardValue.King
    case "A" => CardValue.Ace
  }

}
