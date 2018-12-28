package de.htwg.se.durak.aview.gui

import scalafx.application.JFXApp
//import scalafxml.core.{NoDependencyResolver, FXMLView}

object SFXGui extends JFXApp {

  val resource = getClass.getResource("schbebsi.fxml")
  if (resource == null) {
    throw new Exception("hi")
  }

  //val root = FXMLView(resource, NoDependencyResolver)

}
