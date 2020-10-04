ThisBuild / organization := "com.agnaldo4j"
ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / name         := "phanes"

lazy val domain = (project in file("domain"))
  .settings(
    name := "Domain",
  )

lazy val api = (project in file("api"))
  .dependsOn(domain)
  .settings(
    name := "RestApi",
    libraryDependencies ++= Seq(
      "com.github.finagle" %% "finchx-core" % "0.32.1",
      "com.github.finagle" %% "finchx-circe" % "0.32.1",
      "io.circe" %% "circe-generic-extras" % "0.13.0"
    )
  )

lazy val root = (project in file("."))
  .aggregate(domain, api)
  .settings(
    name := "Root"
  )