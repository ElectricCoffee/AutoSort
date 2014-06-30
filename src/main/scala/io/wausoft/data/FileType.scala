package io.wausoft.data

import java.io.{File => JavaFile}

object FileType {
  trait Type
  case class File(path: JavaFile) extends Type
  case class Directory(path: JavaFile) extends Type
}
