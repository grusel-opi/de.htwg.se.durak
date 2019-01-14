package de.htwg.se.durak

import com.google.inject.{Guice, Injector}
import de.htwg.se.durak.controller.controllerComponent.ControllerInterface
import de.htwg.se.durak.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.durak.model.gameComponent.gameBaseImpl.Game
import de.htwg.se.durak.aview.Tui
import de.htwg.se.durak.aview.gui.Gui

import scala.io.StdIn._

object Durak {
  val injector: Injector = Guice.createInjector(new DurakModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

  //val controller = new Controller(new Game())
  val tui = new Tui(controller)
  val gui = Gui(controller)
  def main(args: Array[String]): Unit = {


    gui.main(args)

    println("Hello to durak")
    var input: String = ""
    do {
      println("Please enter a command (or help for help): ")
      input = readLine
      tui.processInputLine(input)
    } while (input != "q")
  }
}
