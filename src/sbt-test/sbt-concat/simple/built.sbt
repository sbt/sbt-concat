import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.Import.WebKeys._

organization := "net.ground5hark.sbt"

name := "sbt-concat-test"

version := "0.1.9"

scalaVersion := "2.10.6"

lazy val root = (project in file(".")).enablePlugins(SbtWeb)

Concat.groups := Seq(
  "style-group.css" -> group(Seq("css/style1.css", "css/style2.css")),
  "script-group.js" -> group(Seq("js/file1.js", "js/file2.js")),
  "style-libs.css" -> group((sourceDirectory.value / "main" / "assets" / "css" / "libs") * "*.css")
)

pipelineStages := Seq(concat)

val verifyConcatContents = taskKey[Unit]("Verify contents of concatenation groups")

verifyConcatContents := {
  val pub = webTarget.value
  val concatCss = (pub / "" ** "*style-group.css").get
  val concatJs = (pub / "" ** "*script-group.js").get
  val concatLibCss = (pub / "" ** "*style-libs.css").get
  def assertEqual(f: File, contains: Seq[String]): Unit = {
    val contents = IO.read(f)
    contains.foreach { s =>
      if (!contents.contains(s))
        sys.error(s"${f.getName}: Expected `$s` to be in `$contents`")
    }
  }
  val containsCss = Seq("background-color: #000;", "/** css/style2.css **/",
                        "/** css/style1.css **/", "font-face: Arial, sans-serif;")
  assertEqual(concatCss.head, containsCss)
  val containsJs = Seq("file1Callback = function() {", "/** js/file2.js **/",
                       "console.log('file2 - callback called');")
  assertEqual(concatJs.head, containsJs)
  val containsLibCss = Seq("font-size: 15em;", "/** css/libs/style2.css **/",
    "/** css/libs/style1.css **/", "font-weight: bold;")
  assertEqual(concatLibCss.head, containsLibCss)
}

val verifyAssetFiles = taskKey[Unit]("Verify that concat groups are files")

verifyAssetFiles := {
  val pub = webTarget.value
  val concatCss = (pub / "" ** "*style-group.css").get
  val concatJs = (pub / "" ** "*script-group.js").get
  val concatLibCss = (pub / "" ** "*style-libs.css").get
  Seq(concatCss, concatJs, concatLibCss).foreach { f =>
    val file = f.head
    if (!file.isFile || IO.read(file).isEmpty)
      sys.error(s"$file was not a file or was empty")
  }
}

