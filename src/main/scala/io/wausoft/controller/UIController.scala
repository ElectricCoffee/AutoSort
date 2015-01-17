package io.wausoft.controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{ListView, TextField}
import javafx.scene.layout.GridPane
import javafx.stage.{DirectoryChooser, Stage, FileChooser}
import io.wausoft.core.FileHandler
import io.wausoft.data.{SettingsContainer, SettingsAdministrator}

class UIController {
  // Must be vars since they contain mutable data handled by JavaFX
  @FXML private var root: GridPane = _
  @FXML private var destinationFolderField: TextField = _
  @FXML private var settingsListBox: ListView[String] = _

  private var folderPath = ""
  private var settings: SettingsContainer = _
  private lazy val currentWindow = root.getScene.getWindow

  /**
   * Code that runs whenever the window opens
   * @param event The event passed in by JavaFX
   */
  @FXML protected def initialize(event: ActionEvent): Unit = {
    settings = FileHandler.currentSettings // set the local settings object to the current settings
    // add code to populate the list box with the current settings
  }

  /**
   * Causes a popup that enables the user to locate a folder they wish to use as the autosort folder
   * @param event The event passed in by JavaFX
   */
  @FXML protected def locateDestinationFolder(event: ActionEvent): Unit = {
    val chooser = new DirectoryChooser
    chooser setTitle "Choose Working Dictionary"
    val folder = chooser showDialog currentWindow
    folderPath = if (folder != null) folder.getPath else ""
    destinationFolderField setText folderPath
  }

  /**
   * Causes a popup with the basic settings to appear; upon successful entry, then updates the GUI
   * @param event The event passed in by JavaFX
   */
  @FXML protected def onAddSettingClick(event: ActionEvent): Unit = {

  }

  /**
   * Removes the setting from the GUI
   * @param event The event passed in by JavaFX
   */
  @FXML protected def onRemoveSettingClick(event: ActionEvent): Unit = {

  }

  /**
   * Closes the window, discarding any changes made to the settings object
   * @param event The event passed in by JavaFX
   */
  @FXML protected def onCancelButtonClick(event: ActionEvent): Unit = currentWindow.asInstanceOf[Stage].close()

  /**
   * Writes all the changes to the settings list
   * @param event The event passed in by JavaFX
   */
  @FXML protected def onOkButtonClick(event: ActionEvent): Unit = {
    if (!folderPath.isEmpty) { // don't write a file if the folder path is empty; we don't want to create garbage files
      SettingsAdministrator.serializeSettingsObject(FileHandler.settingsFile, settings)
    }
    currentWindow.asInstanceOf[Stage].close()
  }
}
