package io.wausoft.data

import java.io.File
import org.apache.commons.io.{FilenameUtils, FileUtils}
import net.liftweb._

object SettingsAdministrator {
  def deserializeSettingsFile(path: String): SettingsContainer = {
    val ext = FilenameUtils getExtension path
    require(ext.toLowerCase == "json") // make sure only json files can be read
    val content = FileUtils.readFileToString(new File(path), "UTF-8")
    json.parse(content).extract[SettingsContainer]
  }

  def executeSettings(settings: SettingsContainer): Unit = {
    // TODO: Add what needs to be done with the de-serialised settings data
  }
}
