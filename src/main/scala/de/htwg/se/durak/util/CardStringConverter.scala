package de.htwg.se.durak.util

object CardStringConverter {
  //noinspection ScalaStyle
  val excluded = List(0x1F0BF, 0x1F0AC, 0x1F0BC, 0x1F0CC, 0x1F0CF, 0x1F0DC)
  val list: List[String] = (0x1F0A1 to 0x1F0DE).filterNot(i => excluded.contains(i)).map(Character.toChars(_).mkString).toList

}
