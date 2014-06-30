package io.wausoft.test

import org.apache.commons.io.FileUtils
import org.scalatest.FlatSpec
import io.wausoft.data.RichPath.{RichFile, RichString}
import io.wausoft.data._
import io.wausoft.core.FileHandler
import java.io.File

class DataSpec extends FlatSpec {
  def fixture = new {
    val testFolder = RichPath.homeDir / "Desktop" / "TestFolder"
    val testFile = testFolder / "test.json"
    val folderA = testFolder / "FolderA"
    val folderB = testFolder / "FolderB"
    def mkTestDir(): Unit = {
      for(i <- 1 to 10) FileHandler ensureFile (folderA / s"file$i.txt", 'file)
      FileHandler ensureFile (folderB, 'dir)
    }
  }

  "Rich Path" should "create the same path as java.io.File" in {
    val home = System.getProperty("user.home")
    val dir1 = new File(new File(home, "Setting"), "Closures") // without rich path
    val dir2 = home / "Setting" / "Closures" // with rich path
    assert(dir1 === dir2)
  }

  "EnsureFile" must "ensure the existence of a file or folder" in {
    val f = fixture
    FileHandler ensureFile (f.testFolder, 'dir)
    assert(f.testFolder.exists, "because the folder did not exist")
    f.testFolder.delete // delete folder after test passes, we don't need it
  }

  it must "ensure the existence of an empty settings file" in {
    val f = fixture
    if(f.testFile.exists) f.testFile.delete // if it's there, delete it so we can get a clean test
    FileHandler ensureFile (f.testFile, 'file)
    assert(f.testFile.exists)
    val content  = FileUtils readFileToString f.testFile
    val required =
      s"""{
         |  "autosort-folder-path": "${(RichPath.homeDir / "Desktop" / "AutoSort").toEscapedString}",
         |  "local-settings": []
         |}""".stripMargin

    assert(content === required)
  }

  "DeleteFolder" must "successfully delete a folder and all its content" in {
    val f = fixture
    assert(FileHandler deleteFolder (f.testFolder, recursive = true))
  }

  it must "return false and spare anything that isn't a folder" in {
    val testFile = RichPath.homeDir / "Desktop" / "test.txt"
    FileUtils writeStringToFile (testFile, "")
    assert(!(FileHandler deleteFolder testFile))
    testFile.delete
  }

  "moveFiles" must "successfully move all the files from one folder to another" in {
    val f = fixture
    f.mkTestDir() // create working directory
    val result = FileHandler.moveFiles(f.folderA, f.folderB)
    assert(result)
    FileHandler.deleteFolder(f.testFolder, recursive = true)
  }

  it must "successfully move files based on predicate" in {
    val f = fixture
    f.mkTestDir()
    val result = FileHandler.moveFiles(f.folderA, f.folderB, _.getName contains "1")
    assert(result)
    FileHandler.deleteFolder(f.testFolder, recursive = true)
  }
}
