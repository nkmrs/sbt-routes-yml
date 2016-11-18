name := "test-project"

version := "0.1-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(SbtRoutesYmlPlugin)

libraryDependencies ++= Seq(
  "org.yaml" % "snakeyaml" % "1.18"
)
