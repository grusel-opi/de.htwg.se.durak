import CardColor._
import CardValue._

case class Card(val suit: Suit, val value: Pip) {
  override def toString(): String = value + " of " + suit
}
