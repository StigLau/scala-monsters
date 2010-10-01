import sbt._
//import fi.jawsy.sbtplugins.jrebel.JRebelWebPlugin

class ScalaMonstersProject(info: ProjectInfo) extends ParentProject(info) //with JRebelWebPlugin
{
  //  override def managedStyle = ManagedStyle.Maven
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"

  trait MavenFriendly extends DefaultProject {
    //For running JUnit tests
    val junitInterface = "com.novocode" %% "junit-interface" % "0.4.0" from "http://github.com/downloads/bryanjswift/junit-interface/junit-interface-0.4.0.jar"
    override def testFrameworks = super.testFrameworks ++ List(new TestFramework("com.novocode.junit.JUnitFrameworkNoMarker"))
    val scala_swing = "org.scala-lang" % "scala-swing" % "2.8.0"
    val scalatest = "org.scalatest" % "scalatest" % "1.2"
  }
  
  lazy val core = project("core", "scala-monsters-core", new Core(_) with MavenFriendly {val junit = "junit" % "junit" % "4.8.1"})
  lazy val simpleGui = project("simple-gui", "scala-monsters-simple-gui", new SimpleGui(_) with MavenFriendly, core)

  class Core(info: ProjectInfo) extends DefaultProject(info) {}
  class SimpleGui(info: ProjectInfo) extends DefaultProject(info) {

  }

}
