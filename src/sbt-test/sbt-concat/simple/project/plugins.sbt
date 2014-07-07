libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"

addSbtPlugin("com.typesafe.sbt" %% "sbt-web" % "1.0.2")

addSbtPlugin("net.ground5hark.sbt" %% "sbt-concat" % sys.props("project.version"))
