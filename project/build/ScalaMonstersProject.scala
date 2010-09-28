/**
 * Copyright (C) 2010, Mikko Peltonen, Jon-Anders Teigen
 * Licensed under the new BSD License.
 * See the LICENSE file for details.
 */

import sbt._

class ScalaMonstersProject(info:ProjectInfo) extends ParentProject(info) {
//  override def managedStyle = ManagedStyle.Maven

  lazy val core = project("core", "scala-monsters-core")
  lazy val simpleGui = project("simple-gui", "scala-monsters-simple-gui", core)
}
