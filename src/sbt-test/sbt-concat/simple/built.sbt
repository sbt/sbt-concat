import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.Import.WebKeys._

organization := "net.ground5hark.sbt"

name := "sbt-concat-test"

version := "1.0.0"

scalaVersion := "2.10.4"

lazy val root = (project in file(".")).enablePlugins(SbtWeb)

Concat.groups := Seq(
  ("style-group.css", Seq("style1.css", "style2.css")),
  ("script-group.js", Seq("file1.js", "file2.js"))
)

pipelineStages := Seq(concat)

val verifyConcatContents = taskKey[Unit]("Verify contents of concatenation groups")

verifyConcatContents := {
  val pub = (public in Assets).value
  val concatCss = (pub / "" ** "*style-group.css").get
  val concatJs = (pub / "" ** "*script-group.js").get
  def assertEqual(f: File, expected: String): Unit = {
    val contents = IO.read(f)
    if (contents.trim != expected.trim)
      sys.error(s"${f.getName}: Expected `$expected`, but was `$contents`")
  }
  val expectedCss = """
    |/** style1.css **/
    |body {
    |    color: #fff;
    |    background-color: #000;
    |}
    |/** style2.css **/
    |footer {
    |    font-face: Arial, sans-serif;
    |    font-size: 16px;
    |}""".stripMargin('|')
  assertEqual(concatCss.head, expectedCss)
  val expectedJs = """
    |/** file1.js **/
    |var file1Callback = function() {
    |    console.log('file1 - callback called');
    |};
    |/** file2.js **/
    |function file2Callback() {
    |    console.log('file2 - callback called');
    |};""".stripMargin('|')
  assertEqual(concatJs.head, expectedJs)
}