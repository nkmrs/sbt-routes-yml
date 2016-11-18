lazy val root = (project in file(".")).settings(
  version in ThisBuild := "0.0.1",
  organization in ThisBuild := "com.github.nkmrs",
  sbtPlugin := true,
  name := "sbt-routes-yml",
  description := "SBT plugin to integrate with Clover for Java code coverage",
  libraryDependencies ++= Seq(
    "org.yaml" % "snakeyaml" % "1.18",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
)
