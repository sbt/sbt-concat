package net.ground5hark.sbt.concat

import java.io.File
import xsbti.FileConverter
import xsbti.VirtualFileRef
import xsbti.HashedVirtualFileRef

private[concat] object SbtConcatCompat {
  def toFile(f: VirtualFileRef)(using converter: FileConverter): File =
    converter.toPath(f).toFile
  def toFileRef(f: File)(using converter: FileConverter): HashedVirtualFileRef =
    converter.toVirtualFile(f.toPath)
}
