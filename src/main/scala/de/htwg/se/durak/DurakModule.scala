package de.htwg.se.durak

import com.google.inject.AbstractModule
import de.htwg.se.durak.controller.controllerComponent.ControllerInterface
import de.htwg.se.durak.controller.controllerComponent.controllerBaseImpl
import de.htwg.se.durak.model.gameComponent.GameInterface
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.Game
import net.codingwell.scalaguice.ScalaModule

class DurakModule extends AbstractModule with ScalaModule {
  def configure(): Unit = {
    bind[ControllerInterface].to[controllerBaseImpl.Controller]
    bind[GameInterface].to[Game]
  }
}
