sbt-concat
==========
[![Build Status](https://api.travis-ci.org/ground5hark/sbt-concat.png?branch=master)](https://travis-ci.org/ground5hark/sbt-concat)

[sbt-web] plugin for concatenating files together, using the sbt-web asset pipeline.

Plugin
======
Add the plugin to your `project/plugins.sbt`:
```scala
addSbtPlugin("net.ground5hark.sbt" % "sbt-concat" % "0.1.8")
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
Below is an example of specifying concat groups within your `build.sbt` file. You can use `PathFinder` objects or a
`Seq[String]` to specify the files to concatenate together.

```scala
Concat.groups := Seq(
  "style-group.css" -> group(Seq("css/style1.css", "css/style2.css")),
  "script-group.js" -> group(Seq("js/script1.js", "js/script2.js")),
  "style-group2.css" -> group((sourceDirectory.value / "assets" / "style") * "*.css")
)
```

Note that with a `PathFinder`, you will need to take care to ensure that the files it selects will be concatenated in
the order that you desire.

This will produce three files with concatenated contents:

`style-group.css`
```css
/** css/style1.css **/
body { color: #000; }
/** css/style2.css **/
#main { background-color: #fff; }
```

`script-group.js`
```javascript
/** js/script1.js **/
function onDomReady(){ ... }
/** js/script2.js **/
$(onDomReady);
```

`style-group2.css`
```css
/** assets/style/main.css **/
body { font-weight: bold; }
/** assets/style/base.css **/
section { font-size: 15em; }
```

These will reside under the asset build directory in the base target directory by default. You can change the name
of this directory using the `Concat.parentDir` `SettingKey`.

License
=======
This code is licensed under the [MIT License].

[sbt-web]:https://github.com/sbt/sbt-web
[MIT License]:http://opensource.org/licenses/MIT
[Sonatype releases]:https://oss.sonatype.org/content/repositories/releases/
