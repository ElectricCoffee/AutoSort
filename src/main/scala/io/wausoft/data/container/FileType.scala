package io.wausoft.data.container

import java.io.{File => JavaFile}

trait FileType
case class File(file: JavaFile) extends FileType {
  // the if statement allows us to box the java file with a 'promised path'
  // so even if the file doesn't actually exist, we tell the program that we expect it to be a file, not a folder
  // if it however DOES exist, we'll ask the program to bitch about it if the type is wrong
  if(file.exists) require(file.isFile)
}
case class Directory(dir: JavaFile) extends FileType {
  if(dir.exists) require(dir.isDirectory)
}
