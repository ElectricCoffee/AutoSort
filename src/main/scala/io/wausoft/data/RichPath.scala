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

  /**
   * Kind of a cop-out, but it deals nicely with File instances that have a risk of being null
   * @param left an Option File, can be either Some[File] or None
   */
  implicit class OptionalFile(left: Option[File]) {

    /**
     * Returns the path of a File instance if it exists, otherwise it returns whatever the user specified
     * @param right The string to return instead, if the value is None
     * @return String path based on instance of java.io.File, if one exists
     */
    def getPathOrElse(right: String): String = left match {
      case Some(file) => file.getPath
      case None => right
    }

    /**
     * Returns the path of the java.io.File, if and only if it exists
     * Otherwise it'll throw a java.util.NoSuchElementException
     * @return String path of an instance of java.io.File
     */
    def getPath: String = left.get.getPath
  }
}