package io.wausoft.data
import java.io.File

/**
 * A completely OS-Agnostic way of dealing with java.io.File paths
 */
object RichPath {
  val homeDir = System.getProperty("user.home").toFile

  sealed class RichBase[+A](left: A) {
    /**
     * Simple enrichment method designed to let you create a java.io.File
     * by simply writing "folderA" / "folderB" / "folderC"
     * @param right the right-side string
     * @return new file with the given path
     */
    def /(right: String): File = new File(left.toString, right)
  }

  /**
   * Enriched File class, lets you write
   * someFile / "String" to create a file with that kind of path
   * @param left what's written on the left side of the /
   */
  implicit class RichFile(left: File) extends RichBase(left) {
    /**
     * Replaces all the \ in a path name with \\
     * This is useful if you want to avoid the first letter in each subfolder being escaped
     * Example: "C:\Users\Username\Desktop" becomes "C:\\User\\Username\\Desktop"
     * This makes it compatible with json files, as well as in-program strings
     * @return an escaped representation of the string path
     */
    def toEscapedString: String = left.toString.replaceAllLiterally("\\", "\\\\")
  }

  /**
   * Enriched String class, lets you write
   * "someString" / "String" to create a file with that kind of path
   * @param left what's written on the left side of the /
   */
  implicit class RichString(left: String) extends RichBase(left) {
    /**
     * Provides a mean to turn a string into a file
     * @return File based on string
     */
    def toFile: File = new File(left)
  }
}