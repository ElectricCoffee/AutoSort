package io.wausoft.data

import java.io.File
import net.liftweb.json.{DefaultFormats, parse, Serialization}
import org.apache.commons.io.{FilenameUtils, FileUtils}

object SettingsAdministrator {
  private implicit val formats = DefaultFormats // required for .extract to work
  /**
   * Turns the data in the settings file into instances of case classes
   * @param file The settings file
   * @return case class holding the relevant data
   */
  def deserializeSettingsFile(file: File): SettingsContainer = {
    val ext = FilenameUtils getExtension file.toString
    require(ext.toLowerCase == "json") // make sure only json files can be read
    val content = FileUtils.readFileToString(file, "UTF-8")
    parse(content).extract[SettingsContainer]
  }

  /** *
    * Turns the contents of a SettingsContainer into a json file
    * @param file the file you want to input to
    * @param container the settings object you want to serialise
    */
  def serializeSettingsObject(file: File, container: SettingsContainer): Unit =
    FileUtils.writeStringToFile(file, Serialization.write(container), "UTF-8")
}
