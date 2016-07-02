name := "Qidian"

version := "1.0"

lazy val `qidian` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

// Play Slick
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "mysql" % "mysql-connector-java" % "5.1.39"
)

// Database Logger
libraryDependencies ++= Seq(
  "com.mchange" % "c3p0" % "0.9.2.1"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

// Remove all unnecessary documents
sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false


// Swagger Api
// resolvers += Resolver.jcenterRepo
resolvers += "http://jcenter.bintray.com/"

libraryDependencies +=  "com.iheart" %% "play-swagger" % "0.3.3-PLAY2.5"

libraryDependencies += "org.webjars" % "swagger-ui" % "2.1.4"