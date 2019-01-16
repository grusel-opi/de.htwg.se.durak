package de.htwg.se.durak.model.cardComponent.cardBaseImpl

object CardValue extends Enumeration with Ordered[Enumeration#Value] {
  type CardValue = Value
  val Zwei, Drei, Vier, Fünf, Sechs, Sieben, Acht, Neun, Bube, Dame, König, Zehn, Ass = Value

  override def compare(that: Enumeration#Value): Int = that match {
    case x if x.id <  this.Value.id => 1
    case x if x.id >  this.Value.id => -1
    case x if x.id == this.Value.id => 0
  }
}
