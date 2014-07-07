sbt-concat
==========
[sbt-web] plugin for concatenating files together, using the sbt-web asset pipeline.

Plugin
======
Add the plugin to your `project/plugins.sbt`:
```
addSbtPlugin(“net.ground5hark.sbt” % “sbt-concat” % “1.0.0”)
```

Enable the [sbt-web] plugin for your project:
```
lazy val root = (project in file(".")).enablePlugins(SbtWeb)
```

Add the `concat` task to your asset pipeline in your `build.sbt`:
```
pipelineStages := Seq(concat)
```

Configuration options
=====================
Specifying concat groups
------------------------
Below is an example of specifying concat groups within your `build.sbt` file:
```
Concat.groups := Seq(
  ("style-group.css", Seq("style1.css", "style2.css")),
  ("script-group.js", Seq("file1.js", "file2.js"))
)
```

This will produce two files with concatenated contents, `style-group.css` and `script-group.js`.

[sbt-web]:https://github.com/sbt/sbt-web
