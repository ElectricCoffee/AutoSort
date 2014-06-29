package io.wausoft.data

import java.io.File
import net.liftweb.json.{DefaultFormats, parse}
import org.apache.commons.io.{FilenameUtils, FileUtils}

object SettingsAdministrator {
  def deserializeSettingsFile(file: File): SettingsContainer = {
    implicit val formats = DefaultFormats // required for .extract to work
    val ext = FilenameUtils getExtension file.toString
    require(ext.toLowerCase == "json") // make sure only json files can be read
    val content = FileUtils.readFileToString(file, "UTF-8")
    parse(content).extract[SettingsContainer]
  }
}
