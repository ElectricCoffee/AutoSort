package io.wausoft.data

object FileType {
  trait Type
  case object File extends Type
  case object Directory extends Type
}
