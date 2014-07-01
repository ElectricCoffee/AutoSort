package io.wausoft.test

import org.scalatest.FlatSpec
import org.apache.commons.io.FileUtils
import io.wausoft.data._
import io.wausoft.data.container._
import io.wausoft.data.RichPath._
import io.wausoft.core.FileHandler
import java.io.{File => JavaFile}

class DataSpec extends FlatSpec {
  def fixture = new {
    val testFolder = RichPath.homeDir / "Desktop" / "TestFolder"
    val testFile   = testFolder / "test.json"
    val folderA    = testFolder / "FolderA"
    val folderB    = testFolder / "FolderB"

    def mkTestDir(): Unit = {
      for(i <- 1 to 10) FileHandler ensure File(folderA / s"file$i.txt")
      FileHandler ensure Directory(folderB)
    }

    def cleanup(): Unit = FileHandler.deleteFolder(testFolder, recursive = true)
  }

  "Rich Path" should "create the same path as java.io.File" in {
    val home = System.getProperty("user.home")
    val dir1 = new JavaFile(new JavaFile(home, "Setting"), "Closures") // without rich path
    val dir2 = home / "Setting" / "Closures" // with rich path
    assert(dir1 === dir2)
  }

  "EnsureFile" must "ensure the existence of a file or folder" in {
    val f = fixture
    FileHandler ensure Directory(f.testFolder)
    assert(f.testFolder.exists === true, "because the folder did not exist")
    f.testFolder.delete // delete folder after test passes, we don't need it
  }

  it must "ensure the existence of an empty settings file" in {
    val f = fixture
    if(f.testFile.exists) f.testFile.delete // if it's there, delete it so we can get a clean test
    FileHandler ensure File(f.testFile)
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
    FileHandler deleteFolder (f.testFolder, recursive = true)
    assert(f.testFolder.exists === false)
  }

  it must "return false and spare anything that isn't a folder" in {
    val testFile = RichPath.homeDir / "Desktop" / "test.txt"
    FileUtils writeStringToFile (testFile, "")
    intercept[java.io.IOException] {
      FileHandler deleteFolder testFile
    }
    assert(testFile.exists === true)
    testFile.delete
  }

  "moveFiles" must "successfully move all the files from one folder to another" in {
    val f = fixture
    f.mkTestDir() // create working directory
    FileHandler moveData MoveDir(f.folderA, f.folderB)
    assert(f.folderA.listFiles.length === 0)
    assert(f.folderB.listFiles.length === 10) // make sure that the files have actually been moved
    f.cleanup() // nuke the folder recursively, we don't need it anymore
  }

  it must "successfully move files based on predicate" in {
    val f = fixture
    f.mkTestDir()
    FileHandler.moveData(MoveDir(f.folderA, f.folderB), _.getName contains "1")
    // only 2 file names contain 1, that being file1.txt and file10.txt
    // meaning we can assume folderA only has 8 files left
    assert(f.folderA.listFiles.length === 8)
    assert(f.folderB.listFiles.length === 2) // and that folderB contains 2 because of that
    f.cleanup() // nuke the folder recursively, we don't need it anymore
  }

  "MoveDir and MoveFile" should "Throw IllegalArgumentException if given the wrong kind of file" in {
    val f = fixture
    val from = f.testFolder / "from-dir"
    val to = f.testFolder / "to-dir"
    FileHandler ensure File(f.testFile)
    FileHandler ensure Directory(from)
    FileHandler ensure Directory(to)

    intercept[java.lang.IllegalArgumentException] {
      MoveDir(f.testFile, to)
    }
    intercept[java.lang.IllegalArgumentException] {
      MoveFile(from, to)
    }
    f.cleanup()
  }
}
