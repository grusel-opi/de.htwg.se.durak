package de.htwg.se.durak.model

object CardValue extends Enumeration with Ordered[Enumeration#Value] {
  type CardValue = Value
  val Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace = Value

  override def compare(that: Enumeration#Value): Int = that match {
    case x if x.id <  this.Value.id => 1
    case x if x.id >  this.Value.id => -1
    case x if x.id == this.Value.id => 0
  }
}
