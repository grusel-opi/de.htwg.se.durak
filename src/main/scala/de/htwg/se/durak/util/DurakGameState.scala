package de.htwg.se.durak.util

import de.htwg.se.durak.model.{Card, Deck, Player, Turn}
import de.htwg.se.durak.util.DurakGameStateType.DurakGameStateType

case class DurakGameState(players: List[Player], deck: Deck, trump: Card, currentTurn: Turn, active: Player, ok: List[Player], kind: DurakGameStateType) {

}
