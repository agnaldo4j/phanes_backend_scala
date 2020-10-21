ThisBuild / organization := "com.agnaldo4j"
ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / name         := "phanes"

lazy val domain = (project in file("domain"))
  .settings(
    name := "Domain",
  )

lazy val adapters = (project in file("adapters"))
  .dependsOn(domain)
  .settings(
    name := "Adapters",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.4.0",
    )
  )

lazy val config = (project in file("config"))
  .dependsOn(adapters)
  .settings(
    name := "Config",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.4.0",
    )
  )

lazy val quillPersistence = (project in file("quill-persistence"))
  .dependsOn(config)
  .settings(
    name := "QuillPersistence",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.2.17",
      "io.getquill" %% "quill-jdbc" % "3.5.3"
    )
  )

lazy val useCase = (project in file("usecase"))
  .dependsOn(adapters)
  .settings(
    name := "UseCase",
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.0",
      "org.scalatest" %% "scalatest" % "3.2.0" % "test",
      "org.scalatest" %% "scalatest-freespec" % "3.2.0" % "test"
    )
  )

lazy val eventBus = (project in file("eventbus"))
  .dependsOn(useCase)
  .settings(
    name := "EventBus",
  )

lazy val restApi = (project in file("restapi"))
  .dependsOn(quillPersistence, eventBus)
  .settings(
    name := "RestApi",
    libraryDependencies ++= Seq(
      "com.github.finagle" %% "finchx-core" % "0.32.1",
      "com.github.finagle" %% "finchx-circe" % "0.32.1",
      "io.circe" %% "circe-generic-extras" % "0.13.0",
    )
  )

lazy val phanes = (project in file("."))
  .aggregate(config, adapters, domain, quillPersistence, useCase, eventBus, restApi)
  .settings(
    name := "Phanes"
  )