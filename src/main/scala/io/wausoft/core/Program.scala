package io.wausoft.core

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Scene, Parent}
import javafx.stage.Stage

object Program {
  def main(args: Array[String]): Unit = Application.launch(classOf[Program], args: _*)
}

class Program extends Application {
  override def start(stage: Stage): Unit = {
    val loader = new FXMLLoader(classOf[Program] getResource "/MainWindow.fxml")
    val root = loader.load.asInstanceOf[Parent]
    stage setTitle "AutoSort Configuration"
    stage setScene new Scene(root, 570, 370)
    stage.show()
  }
}
