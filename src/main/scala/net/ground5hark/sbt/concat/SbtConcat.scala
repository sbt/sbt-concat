package net.ground5hark.sbt.concat

import com.typesafe.sbt.web.{PathMapping, SbtWeb}
import sbt.Keys._
import sbt._
import com.typesafe.sbt.web.pipeline.Pipeline
import collection.mutable
import mutable.ListBuffer

object Import {
  val concat = TaskKey[Pipeline.Stage]("web-concat", "Concatenates groups of web assets")

  object Concat {
    val groups = SettingKey[Seq[ConcatGroup]]("web-concat-groups", "List of ConcatGroup items")
  }
}

object NotHiddenFileFilter extends FileFilter {
  override def accept(f: File): Boolean = !HiddenFileFilter.accept(f)
}

object SbtConcat extends AutoPlugin {
  override def requires = SbtWeb

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtWeb.autoImport._
  import WebKeys._
  import autoImport._
  import Concat._

  override def projectSettings = Seq(
    groups := ListBuffer.empty[ConcatGroup],
    includeFilter in concat := NotHiddenFileFilter,
    concat := concatFiles.value
  )

  private def concatFiles: Def.Initialize[Task[Pipeline.Stage]] = Def.task {
    mappings: Seq[PathMapping] =>
      val groupsValue = groups.value

      if (groupsValue.nonEmpty) {
        streams.value.log.info(s"Building ${groupsValue.size} concat group(s)")
        val reverseMapping = ReverseGroupMapping.get(groupsValue, streams.value.log)
        val concatGroups = mutable.Map.empty[String, StringBuilder]
        mappings.foreach {
          case (mappingFile, mappingName) =>
            val mappingBaseName = util.baseName(mappingName)
            if (mappingFile.isFile)
              // Iterate through each entry until a match is found
              reverseMapping.takeWhile {
                case (reverseFileName, reverseGroupName) =>
                  val matches = util.baseName(reverseFileName).equals(mappingBaseName)
                  if (matches) {
                    concatGroups.getOrElseUpdate(reverseGroupName, new StringBuilder)
                      .append(s"\n/** $mappingBaseName **/\n")
                      .append(IO.read(mappingFile))
                    reverseMapping.remove(reverseFileName)
                  }
                  !matches
              }
        }

        val targetDir = (public in Assets).value / "concat"
        concatGroups.map {
          case (groupName, concatenatedContents) =>
            val outputFile = targetDir / groupName
            IO.write(outputFile, concatenatedContents.toString)
            (outputFile, s"concat/$groupName")
        }.toSeq
      } else {
        Seq.empty
      } ++ mappings
  }
}

private object util {
  def baseName(filePath: String): String = new File(filePath).getName
}

private object ReverseGroupMapping {
  def get(groups: Seq[ConcatGroup], logger: Logger): mutable.Map[String, String] = {
    val ret = mutable.Map.empty[String, String]
    groups.foreach {
      case (groupName, fileNames) => fileNames.foreach { fileName =>
        ret(fileName) = groupName
      }
    }
    ret
  }
}
