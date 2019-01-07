package de.htwg.se.durak.model.exceptions

class VictimHasNotEnoughCardsToBlockException(val message: String = "Victim has not enough cards to block!") extends
  Exception(message)
