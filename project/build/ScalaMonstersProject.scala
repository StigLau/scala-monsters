import sbt._

class ScalaMonstersProject(info: ProjectInfo) extends ParentProject(info)
{
  //  override def managedStyle = ManagedStyle.Maven
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"

  trait MavenFriendly extends DefaultProject {
	val junitInterface = "com.novocode" % "junit-interface" % "0.6" % "test->default"

    override def testFrameworks = super.testFrameworks ++ List(new TestFramework("com.novocode.junit.JUnitFrameworkNoMarker"))
    val scala_swing = "org.scala-lang" % "scala-swing" % "2.8.0"
    val scalatest = "org.scalatest" % "scalatest" % "1.2"
  }
  
  lazy val core = project("core", "scala-monsters-core", new DefaultProject(_) with MavenFriendly {val junit = "junit" % "junit" % "4.8.1"})
  lazy val simpleGui = project("simple-gui", "scala-monsters-simple-gui", new DefaultProject(_) with MavenFriendly, core)

}
