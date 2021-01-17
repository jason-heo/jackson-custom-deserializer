name := "jackson-custom-deserializer"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % "2.11.2",

  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.2"
)