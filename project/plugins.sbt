libraryDependencies <++= (sbtVersion) {
  sv => Seq(
    "org.scala-sbt" % "scripted-plugin" % sv
  )
}
