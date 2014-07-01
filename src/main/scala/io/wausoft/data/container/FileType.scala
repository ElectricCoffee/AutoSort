package io.wausoft.data.container

import java.io.{File => JavaFile}

trait FileType
case class File(file: JavaFile) extends FileType
case class Directory(dir: JavaFile) extends FileType {
  def listFiles: List[JavaFile] = dir.listFiles.toList
}
