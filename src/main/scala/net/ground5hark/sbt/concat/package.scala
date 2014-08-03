package net.ground5hark.sbt

import sbt.PathFinder

package object concat {
  type ConcatGroup = (String, Either[Seq[String], PathFinder])
}
