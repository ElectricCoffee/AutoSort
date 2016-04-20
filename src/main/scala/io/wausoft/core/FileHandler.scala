package io.wausoft.core

import io.wausoft.data._
import io.wausoft.data.RichPath._ // enables the use of '/' for files
import io.wausoft.data.container._
import org.apache.commons.io.FileUtils
import java.io.{File => JavaFile}

object FileHandler {
  // making all this lazy so it doesn't freak out with a ton of IO the very moment the class is called,
  // but rather doing the IO when it's needed
  lazy val settingsHome: JavaFile = ensure(Directory(RichPath.homeDir / ".autosort"))
  lazy val settingsFile: JavaFile = ensure(File(settingsHome / "settings.json"))
  def currentSettings = SettingsAdministrator deserializeSettingsFile settingsFile

  /**
   * Makes sure the file is there and returns it
   * @param kind the kind of file we're dealing with, i.e. dir or file
   * @return The specified file
   */
  def ensure(kind: FileType): JavaFile = kind match {
    case Directory(d) => if(!d.exists) d.mkdir; d
    case File(f) => if(!f.exists) generateSettingsFile(f); f
    case _ => throw new Exception("This should not even be possible!!")
  }

  /**
   * Generates an empty settings file at the given path
   * @param file the file path and name of the file we want to generate
   */
  def generateSettingsFile(file: JavaFile): Unit = {
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
   */
  def deleteFolder(folder: JavaFile, recursive: Boolean = false): Unit = if(folder.isDirectory) {
    for(file <- folder.listFiles) { // for each file in the folder
      if(file.isDirectory && recursive) // if the file is a folder, and recursion is on
        deleteFolder(file, recursive)   // delete the folder and all its contents
      file.delete // delete the encountered file
    }
    folder.delete // once done deleting all the files, delete the containing folder
  }
  else throw new java.io.IOException("The provided File was not a folder")

  /**
   * Moves all the files from one folder to another
   * Or move a specific subset of files given a predicate
   * @param movement Is the type of movement, consisting of a file and its destination
   * @param predicate a predicate that designates the rules of whatever files must be moved
   */
  def move(movement: DataMovement, predicate: JavaFile => Boolean = _ => true): Unit = movement match {
    case MoveDir(origin, destination) =>
      for(file <- origin.listFiles if predicate(file)) move(MoveFile(file, destination))
    case MoveFile(file, destination) =>
      FileUtils.moveFileToDirectory(file, destination, !destination.exists)
  }
}
