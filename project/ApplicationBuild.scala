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
  val appOrganization = "com.getgua"
  val appScalaVersion = "2.11.7"

  val qidianLibraryVersion = "1.1.1"

  /**
    * Dependencies
    */

  val commonDependencies = Seq(
    cache,
    ws,
    specs2 % Test
  )

  val qidianDependencies = Seq(
    // add qidian dependency here
    "com.getgua" % "qidian-utils_2.11" % qidianLibraryVersion
  )

  val slickDependencies = Seq(
    jdbc,
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

  /**
    * Resovlers
    */
  val appResovlers = Seq (
    "jcenterRepo" at "http://jcenter.bintray.com/"
  )


  val scalaBuildOptions = Seq("-unchecked", "-deprecation", "-feature", "-language:reflectiveCalls",
    "-language:implicitConversions", "-language:postfixOps", "-language:dynamics","-language:higherKinds",
    "-language:existentials", "-language:experimental.macros", "-Xmax-classfile-name", "140")
  //val scalaBuildOptions = Seq()

  /**
    * TODO: make the setting common in the future
    */
  val commonSettings = Seq(
    organization := appOrganization,
    version := appVersion,
    scalaVersion := appScalaVersion,
    scalacOptions ++= scalaBuildOptions,
    sources in doc in Compile := List(),
    publishArtifact in packageDoc in Compile := false,
    unmanagedResourceDirectories in Test <+= baseDirectory ( _ /"target/web/public/test")
  )

  /**
    * Service and Library
    */
  val utilsProject = Project("qidian-utils", file("services/utils")).settings(
    organization := appOrganization,
    version := qidianLibraryVersion,
    scalaVersion := appScalaVersion,
    scalacOptions ++= scalaBuildOptions,

    libraryDependencies ++= (commonDependencies),

    publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
  )

  val appProject = Project("qidian-app", file("services/app")).enablePlugins(PlayScala).settings(
    organization := appOrganization,
    version := appVersion,
    scalaVersion := appScalaVersion,
    scalacOptions ++= scalaBuildOptions,
    sources in doc in Compile := List(),
    publishArtifact in packageDoc in Compile := false,
    unmanagedResourceDirectories in Test <+= baseDirectory ( _ /"target/web/public/test"),

    libraryDependencies ++= (commonDependencies ++ qidianDependencies ++ slickDependencies  ++ dbLoggerDependencies ++ swagggerApiDependencies),

    javaOptions in Test += "-Dconfig.resource=app.application.conf"
  )

  val cmsProject = Project("qidian-cms", file("services/cms")).enablePlugins(PlayScala).settings(
    organization := appOrganization,
    version := appVersion,
    scalaVersion := appScalaVersion,
    scalacOptions ++= scalaBuildOptions,
    sources in doc in Compile := List(),
    publishArtifact in packageDoc in Compile := false,
    unmanagedResourceDirectories in Test <+= baseDirectory ( _ /"target/web/public/test"),

    libraryDependencies ++= (commonDependencies ++ qidianDependencies ++ slickDependencies),

    javaOptions in Test += "-Dconfig.resource=cms.application.conf"
  )

  val wsProject = Project("qidian-ws", file("services/ws")).enablePlugins(PlayScala).settings(
    organization := appOrganization,
    version := appVersion,
    scalaVersion := appScalaVersion,
    scalacOptions ++= scalaBuildOptions,
    sources in doc in Compile := List(),
    publishArtifact in packageDoc in Compile := false,
    unmanagedResourceDirectories in Test <+= baseDirectory ( _ /"target/web/public/test"),

    libraryDependencies ++= (commonDependencies ++ qidianDependencies ++ slickDependencies),

    javaOptions in Test += "-Dconfig.resource=ws.application.conf"
  )

  val qidianProject = Project("qidian", file(".")).enablePlugins(PlayScala).settings(
    organization := appOrganization,
    version := appVersion,
    scalaVersion := appScalaVersion,
    scalacOptions ++= scalaBuildOptions,
    sources in doc in Compile := List(),
    publishArtifact in packageDoc in Compile := false,
    unmanagedResourceDirectories in Test <+= baseDirectory ( _ /"target/web/public/test"),

    libraryDependencies ++= (commonDependencies)
  ).dependsOn(wsProject % "test->test;compile->compile",
              cmsProject % "test->test;compile->compile",
              appProject % "test->test;compile->compile")
    .aggregate(wsProject, cmsProject, appProject)
}