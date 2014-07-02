package io.wausoft.test

import org.scalatest.{Matchers, FlatSpec}
import org.apache.commons.io.FileUtils
import io.wausoft.core.FileHandler
import io.wausoft.data._
import io.wausoft.data.container._
import io.wausoft.data.RichPath._
import java.io.{File => JavaFile, IOException}

class DataSpec extends FlatSpec with Matchers {
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
    dir1 should equal (dir2)
  }

  "EnsureFile" should "ensure the existence of a file or folder" in {
    val f = fixture
    FileHandler ensure Directory(f.testFolder)
    f.testFolder.exists shouldBe true
  }

  it should "create a file or folder of the correct type" in {
    // this test runs off of the folder we created earlier
    fixture.testFolder should be a 'directory
  }

  it should "ensure the existence of an empty settings file" in {
    val f = fixture
    FileHandler ensure File(f.testFile)
    f.testFile.exists shouldBe true
  }

  it should "ensure the file is indeed a file" in {
    // once again, this goes off the file created in the previous test
    fixture.testFile should be a 'file
  }

  it should "contain pre-generated content equal to the following" in {
    val content  = FileUtils readFileToString fixture.testFile
    val required =
      s"""{
         |  "autosort-folder-path": "${(RichPath.homeDir / "Desktop" / "AutoSort").toEscapedString}",
         |  "local-settings": []
         |}""".stripMargin

    content should equal (required)
  }

  "DeleteFolder" should "successfully delete a folder and all its content" in {
    val f = fixture
    FileHandler deleteFolder (f.testFolder, recursive = true)
    f.testFolder.exists shouldBe false
  }

  it should "throw an exception if directly targeting something that isn't a folder" in {
    val testFile = RichPath.homeDir / "Desktop" / "test.txt"
    FileUtils writeStringToFile (testFile, "")

    an [IOException] should be thrownBy (FileHandler deleteFolder testFile)
    testFile.exists shouldBe true
    testFile.delete
  }

  "move" should "successfully move all the files from one folder to another" in {
    val f = fixture
    f.mkTestDir() // create working directory
    FileHandler move MoveDir(f.folderA, f.folderB)

    f.folderA.listFiles should have length 0
    f.folderB.listFiles should have length 10 // make sure that the files have actually been moved

    f.cleanup() // nuke the folder recursively, we don't need it anymore
  }

  it should "successfully move files based on predicate" in {
    val f = fixture
    f.mkTestDir()
    FileHandler.move(MoveDir(f.folderA, f.folderB), _.getName contains "1")
    // only 2 file names contain 1, that being file1.txt and file10.txt
    // meaning we can assume folderA only has 8 files left
    f.folderA.listFiles should have length 8
    f.folderB.listFiles should have length 2 // and that folderB contains 2 because of that

    f.cleanup() // nuke the folder recursively, we don't need it anymore
  }

  "MoveDir and MoveFile" should "Throw IllegalArgumentException if given the wrong kind of file" in {

    val f = fixture
    val from = f.testFolder / "from-dir"
    val to = f.testFolder / "to-dir"
    FileHandler ensure File(f.testFile)
    FileHandler ensure Directory(from)
    FileHandler ensure Directory(to)

    an [IllegalArgumentException] should be thrownBy MoveDir(f.testFile, to)
    an [IllegalArgumentException] should be thrownBy MoveFile(from, to)

    f.cleanup()
  }
}
