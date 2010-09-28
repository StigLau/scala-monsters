import sbt._
//import fi.jawsy.sbtplugins.jrebel.JRebelWebPlugin

class ScalaMonstersProject(info:ProjectInfo) extends ParentProject(info) //with JRebelWebPlugin
{
//  override def managedStyle = ManagedStyle.Maven
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"


  lazy val core = project("core", "scala-monsters-core")
  lazy val simpleGui = project("simple-gui", "scala-monsters-simple-gui", core)
}
