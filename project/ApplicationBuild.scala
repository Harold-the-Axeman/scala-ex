import sbt._
import sbt.Keys._
import play.sbt.PlayImport._
import play.sbt.PlayScala

object ApplicationBuild extends Build {
  //val branch = "git rev-parse --abbrev-ref HEAD".!!.trim
  //val commit = "git rev-parse --short HEAD".!!.trim
  val buildTime = (new java.text.SimpleDateFormat("yyyyMMdd-HHmmss")).format(new java.util.Date())
  //val appVersion = "%s-%s-%s".format(branch, commit, buildTime)
  val appVersion = "1.0"

  val appScalaVersion = "2.11.7"

  val commonDependencies = Seq(
    jdbc,
    cache,
    ws,
    specs2 % Test
  )

  val slickDependencies = Seq(
    "com.typesafe.play" %% "play-slick" % "2.0.0",
    "mysql" % "mysql-connector-java" % "5.1.39"
  )

  val dbLoggerDependencies = Seq(
    "com.mchange" % "c3p0" % "0.9.2.1"
  )

  val swagggerApiDependencies = Seq (
    "com.iheart" %% "play-swagger" % "0.3.3-PLAY2.5",
    "org.webjars" % "swagger-ui" % "2.1.4"
  )

  val appResovlers = Seq (
    "jcenterRepo" at "http://jcenter.bintray.com/"
  )

  val serviceADependencies = Seq() // You can have service specific dependencies
  val serviceBDependencies = Seq()

/*  val scalaBuildOptions = Seq("-unchecked", "-deprecation", "-feature", "-language:reflectiveCalls",
    "-language:implicitConversions", "-language:postfixOps", "-language:dynamics","-language:higherKinds",
    "-language:existentials", "-language:experimental.macros", "-Xmax-classfile-name", "140")*/
  val scalaBuildOptions = Seq()

  val commonSettings = Seq()

  val cmsProject = Project("qidian-cms", file("services/cms")).enablePlugins(PlayScala).settings(
    version := appVersion,
    scalaVersion := appScalaVersion,
    libraryDependencies ++= (commonDependencies ++ slickDependencies),

    javaOptions in Test += "-Dconfig.resource=cms.application.conf"
  )

  val appProject = Project("qidian-app", file("services/app")).enablePlugins(PlayScala).settings(
    version := appVersion,
    scalaVersion := appScalaVersion,
    libraryDependencies ++= (commonDependencies ++ slickDependencies)
  )

  val utilsProject = Project("qidian-utils", file("services/utils")).settings(
    version := appVersion,
    scalaVersion := appScalaVersion,
    libraryDependencies ++= (commonDependencies)
  )

  val qidianProject = Project("qidian", file(".")).enablePlugins(PlayScala).settings(
    version := appVersion,
    scalaVersion := appScalaVersion,
    libraryDependencies ++= (commonDependencies ++ slickDependencies ++ dbLoggerDependencies ++ swagggerApiDependencies),
    // This project runs both services together, which is mostly useful in development mode.
    scalacOptions ++= scalaBuildOptions,
    sources in doc in Compile := List(),
    publishArtifact in packageDoc in Compile := false,
    unmanagedResourceDirectories in Test <+= baseDirectory ( _ /"target/web/public/test" )
  ).dependsOn(cmsProject % "test->test;compile->compile").aggregate(cmsProject)
  //.dependsOn(common % "test->test;compile->compile", serviceA % "test->test;compile->compile", serviceB % "test->test;compile->compile").aggregate(common, serviceA, serviceB)

}