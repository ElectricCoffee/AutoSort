package io.wausoft.core

import io.wausoft.data.SettingsAdministrator
import io.wausoft.data.RichPath.{RichFile, RichString} // enables the use of '/' for files
import org.apache.commons.io.FileUtils
import java.io.File

object FileHandler {
  // making all this lazy so it doesn't freak out with a ton of IO the very moment the class is called,
  // but rather doing the IO when it's needed
  lazy val home = System.getProperty("user.home")
  lazy val dir: File = ensureFile(home / ".autosort", 'dir)
  lazy val fileLocation: File = ensureFile(dir / "settings.json", 'file)
  lazy val settings = SettingsAdministrator deserializeSettingsFile fileLocation

  /**
   * Makes sure the file is there and returns it
   * @param file the file we need to make sure exists
   * @param kind the kind of file we're dealing with, i.e. dir or file
   * @return The specified file
   */
  def ensureFile(file: File, kind: Symbol): File = {
    if (!file.exists && kind == 'dir) { // if directory, make directory
      file.mkdir
    }
    else if(!file.exists && kind == 'file) { // if file, make file template
      generateSettingsFile(file)
    }
    require(file.exists) // file SHOULD exist now, if not, crash.
    file // if nothing crashes, return the file
  }

  /**
   * Generates an empty settings file at the given path
   * @param file the file path and name of the file we want to generate
   */
  def generateSettingsFile(file: File): Unit = {
    val data = // generate empty settings file
      s"""{
         |  "autosort-folder-path": "${(home / "Desktop" / "AutoSort").toEscapedString}",
         |  "local-settings": []
         |}""".stripMargin

    FileUtils writeStringToFile (file, data)
  }

  def deleteFolder(folder: File): Boolean = {
    if(folder.isDirectory) {
      for(file <- folder.listFiles) file.delete
      folder.delete
    }
    else false
  }
}
