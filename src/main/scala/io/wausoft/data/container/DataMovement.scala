package io.wausoft.data.container

import java.io.{File => JavaFile} // so it won't clash with the other File class in the same namespace

trait DataMovement
case class MoveFile(file: JavaFile, destination: JavaFile) extends DataMovement {
    require(file.isFile && destination.isDirectory)
}

case class MoveDir(origin: JavaFile, destination: JavaFile) extends DataMovement {
    require(origin.isDirectory && destination.isDirectory)
}