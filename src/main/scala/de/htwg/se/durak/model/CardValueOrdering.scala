package de.htwg.se.durak.model

import de.htwg.se.durak.model.CardValue.CardValue

object CardValueOrdering extends Ordering[CardValue] {
  override def compare(a: CardValue, b: CardValue): Int = b match {
    case x if x.  < a.id => 1
    case x if x.id  > a.id => -1
    case x if x.id == a.id => 0
  }
}
