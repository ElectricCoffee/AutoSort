package io.wausoft.data

import org.apache.commons.io._

object SettingsParser {
  def parse(path: String): Unit = {
    val ext = FilenameUtils getExtension path
    require(ext.toLowerCase == "json") // make sure only json files can be read
    // TODO: parse the settings file in accordance to the SettingsContainer case class
  }
}
