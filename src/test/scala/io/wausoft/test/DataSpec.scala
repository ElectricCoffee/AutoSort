package io.wausoft.test

import org.apache.commons.io.FileUtils
import org.scalatest.FlatSpec
import io.wausoft.data.RichPath.{RichFile, RichString}
import io.wausoft.core.FileHandler
import java.io.File

class DataSpec extends FlatSpec {
  def fixture = new {
    val testFolder = System.getProperty("user.home") / "Desktop" / "TestFolder"
    val testFile = testFolder / "test.json"
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
    FileHandler ensureFile (f.testFile, 'file)
    assert(f.testFile.exists)
    val content  = FileUtils readFileToString f.testFile
    val required =
      s"""{
         |  "autosort-folder-path": "${(System.getProperty("user.home") / "Desktop" / "AutoSort").toEscapedString}",
         |  "local-settings": []
         |}""".stripMargin

    assert(content === required)
  }

  "DeleteFolder" must "successfully delete a folder and all its content" in {
    val f = fixture
    assert(FileHandler deleteFolder f.testFolder)
  }

  it must "return false and spare anything that isn't a folder" in {
    val testFile = System.getProperty("user.home") / "Desktop" / "test.txt"
    FileUtils writeStringToFile (testFile, "")
    assert(!(FileHandler deleteFolder testFile))
    testFile.delete
  }
}
