package de.htwg.se.durak.util

import de.htwg.se.durak.model.{Card, CardColor, CardValue}
import de.htwg.se.durak.model.CardColor.CardColor
import de.htwg.se.durak.model.CardValue.CardValue

object CardStringConverter {
  val excluded = List(0x1F0BF, 0x1F0AC, 0x1F0BC, 0x1F0CC, 0x1F0CF, 0x1F0DC)
  val list: List[String] = (0x1F0A1 to 0x1F0DE).filterNot(i => excluded.contains(i)).map(Character.toChars(_).mkString).toList

  def parseColorString(input: String): CardColor = input match {
    case "Herz" | "herz"  => CardColor.Herz
    case "Karo" | "karo"  => CardColor.Karo
    case "Kreuz"| "kreuz" => CardColor.Kreuz
    case "Pik"  | "pik"   => CardColor.Pik
  }

  def parseValueString(input: String): CardValue = input match {
    /* case "2" => CardValue.Zwei
    case "3" => CardValue.Drei
    case "4" => CardValue.Vier
    case "5" => CardValue.Fünf
    case "6" => CardValue.Sechs
    case "7" => CardValue.Sieben
    case "8" => CardValue.Acht
    case "9" => CardValue.Neun */
    case "10" => CardValue.Zehn
    // case "B" | "b" | "bube" | "Bube"   => CardValue.Bube
    case "D" | "d" | "dame" | "Dame"   => CardValue.Dame
    case "K" | "k" | "könig" | "König" => CardValue.König
    case "A" | "a" | "ass" | "Ass"     => CardValue.Ass
  }

  def parseColorObject(color: CardColor): String = color match {
    case CardColor.Kreuz => "Kreuz"
    case CardColor.Karo => "Karo"
    case CardColor.Herz => "Herz"
    case CardColor.Pik => "Pik"
  }

  def parseCardObject(card: Card): String = parseColorObject(card.color) + " " + parseValueObject(card.value)


  def parseValueObject(value: CardValue) : String = value match {
    /* case CardValue.Zwei => "2"
    case CardValue.Drei => "3"
    case CardValue.Vier => "4"
    case CardValue.Fünf => "5"
    case CardValue.Sechs => "6"
    case CardValue.Sieben => "7"
    case CardValue.Acht => "8"
    case CardValue.Neun => "9"
    case CardValue.Zehn => "10"
    case CardValue.Bube => "B" */
    case CardValue.Dame => "D"
    case CardValue.König => "K"
    case CardValue.Ass =>"A"
  }

}
