package net.ground5hark.sbt.concat

import xsbti.FileConverter
import java.io.File

private[concat] object SbtConcatCompat {
  def toFile(f: File)(implicit converter: FileConverter): File = f
  def toFileRef(f: File)(implicit converter: FileConverter): File = f
}
