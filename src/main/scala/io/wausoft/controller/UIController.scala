package io.wausoft.controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{ListView, TextField}
import javafx.scene.layout.GridPane
import javafx.stage.{DirectoryChooser, Stage, FileChooser}

class UIController {

  @FXML private var root: GridPane = _

  @FXML private var destinationFolderField: TextField = _

  @FXML private var settingsListBox: ListView[String] = _

  private var folderPath = ""

  private lazy val currentWindow = root.getScene.getWindow

  @FXML protected def locateDestinationFolder(event: ActionEvent): Unit = {
    val chooser = new DirectoryChooser
    chooser setTitle "Choose Working Dictionary"
    val folder = chooser showDialog currentWindow
    folderPath = folder.getPath
    destinationFolderField setText folderPath
  }

  @FXML protected def onAddSettingClick(event: ActionEvent): Unit = {

  }

  @FXML protected def onRemoveSettingClick(event: ActionEvent): Unit = {

  }

  @FXML protected def onCancelButtonClick(event: ActionEvent): Unit = {

  }

  @FXML protected def onOkButtonClick(event: ActionEvent): Unit = {

  }
}
