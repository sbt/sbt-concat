sbt-concat
==========
[![Build Status](https://api.travis-ci.org/ground5hark/sbt-concat.png?branch=master)](https://travis-ci.org/ground5hark/sbt-concat)

[sbt-web] plugin for concatenating files together, using the sbt-web asset pipeline.

Plugin
======
Add the plugin to your `project/plugins.sbt`:
```scala
addSbtPlugin("net.ground5hark.sbt" % "sbt-concat" % "0.1.1")
```

Add the [Sonatype releases] resolver:
```scala
resolvers += Resolver.sonatypeRepo("releases")
```

Enable the [sbt-web] plugin for your project:
```scala
lazy val root = (project in file(".")).enablePlugins(SbtWeb)
```

Add the `concat` task to your asset pipeline in your `build.sbt`:
```scala
pipelineStages := Seq(concat)
```

Configuration options
=====================
### Specifying concat groups
Below is an example of specifying concat groups within your `build.sbt` file:

```scala
Concat.groups := Seq(
  "style-group.css" -> Seq("style1.css", "style2.css"),
  "script-group.js" -> Seq("script1.js", "script2.js")
)
```

This will produce two files with concatenated contents:

`style-group.css`
```css
/** style1.css **/
body { color: #000; }
/** style2.css **/
#main { background-color: #fff; }
```

`script-group.js`
```javascript
/** script1.js **/
function onDomReady(){ ... }
/** script2.js **/
$(onDomReady);
```

These will reside under the asset build directory in a sub-directory named `concat`.

License
=======
This code is licensed under the [MIT License].

[sbt-web]:https://github.com/sbt/sbt-web
[MIT License]:http://opensource.org/licenses/MIT
[Sonatype releases]:https://oss.sonatype.org/content/repositories/releases/
