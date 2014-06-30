package io.wausoft.core

import io.wausoft.data._
import io.wausoft.data.RichPath._ // enables the use of '/' for files
import org.apache.commons.io.FileUtils
import java.io.File

object FileHandler {
  // making all this lazy so it doesn't freak out with a ton of IO the very moment the class is called,
  // but rather doing the IO when it's needed
  lazy val dir: File = ensureFile(FileType.Directory(RichPath.homeDir / ".autosort"))
  lazy val fileLocation: File = ensureFile(FileType.File(dir / "settings.json"))
  lazy val settings = SettingsAdministrator deserializeSettingsFile fileLocation

  /**
   * Makes sure the file is there and returns it
   * @param kind the kind of file we're dealing with, i.e. dir or file
   * @return The specified file
   */
  def ensureFile(kind: FileType.Type): File = kind match {
    case FileType.Directory(d) =>
      if(!d.exists) d.mkdir
      d
    case FileType.File(f) =>
      if(!f.exists) generateSettingsFile(f)
      f
  }

/*  {
    if (!file.exists && kind == FileType.Directory) { // if directory, make directory
      file.mkdir
    }
    else if(!file.exists && kind == FileType.File) { // if file, make file template
      generateSettingsFile(file)
    }
    require(file.exists) // file SHOULD exist now, if not, crash.
    file // if nothing crashes, return the file
  }*/

  /**
   * Generates an empty settings file at the given path
   * @param file the file path and name of the file we want to generate
   */
  def generateSettingsFile(file: File): Unit = {
    val data = // generate empty settings file
      s"""{
         |  "autosort-folder-path": "${(RichPath.homeDir / "Desktop" / "AutoSort").toEscapedString}",
         |  "local-settings": []
         |}""".stripMargin

    FileUtils writeStringToFile (file, data)
  }

  /**
   * Deletes a folder and all its contents
   * @param folder the target folder
   * @param recursive if true, all subfolders will be deleted too
   * @return true if successful
   */
  def deleteFolder(folder: File, recursive: Boolean = false): Boolean = {
    if(folder.isDirectory) {
      for(file <- folder.listFiles) { // for each file in the folder
        if(file.isDirectory && recursive) // if the file is a folder, and recursion is on
          deleteFolder(file, recursive)   // delete the folder and all its contents
        file.delete // delete the encountered file
      }
      folder.delete // once done deleting all the files, delete the containing folder
    }
    else false // if the deletion fails; return false
  }

  /**
   * Moves all the files from one folder to another
   * Or move a specific subset of files given a predicate
   * @param origin the directory the files originate from
   * @param destination the directory the files will be moved to
   * @param predicate a predicate that designates the rules of whatever files must be moved
   * @return returns true if succeeded
   */
  def moveFiles(origin: File, destination: File, predicate: File => Boolean = _ => true): Boolean = {
    if(origin.isDirectory && destination.isDirectory) {
      for(file <- origin.listFiles if predicate(file)) moveFile(file, destination)
      true
    }
    else false
  }

  /**
   * Moves a single file to its destination folder
   * @param file the file
   * @param destination the file's destination
   * @return true if successful
   */
  def moveFile(file: File, destination: File): Boolean = {
    if(file.isFile && destination.isDirectory) {
      val createDir = !destination.exists // if it doesn't exist, create it
      FileUtils.moveFileToDirectory(file, destination, createDir)
      true
    }
    else false
  }
}
