package de.htwg.se.durak.util

import de.htwg.se.durak.model.cardComponent.Card
import de.htwg.se.durak.model.deckComponent.deckBaseImpl.Deck
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.Turn
import de.htwg.se.durak.model.playerComponent.Player

case class DurakGameState(players: List[Player], deck: Deck, trump: Card, currentTurn: Turn, active: Player,
                          ok: List[Player])
