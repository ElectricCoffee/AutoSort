package io.wausoft.core

import java.io.File
import io.wausoft.data.RichPath.{RichFile, RichString}

object Program extends App {
  var home = System.getProperty("user.home")
  val dir1 = new File(new File(home, "Setting"), "Closures") // without rich path
  val dir2 = home / "Setting" / "Closures" // with rich path

  println(dir1 == dir2) // prints "true"
}
