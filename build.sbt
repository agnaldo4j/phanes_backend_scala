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
      "org.springframework" % "spring-context" % "5.2.9.RELEASE",
      "com.typesafe" % "config" % "1.4.0",
    )
  )

lazy val relationalPersistence = (project in file("relational-persistence"))
  .dependsOn(config)
  .settings(
    name := "RelationalPersistence",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.2.17",
      "com.zaxxer" % "HikariCP" % "3.4.5",
      "org.springframework.data" % "spring-data-jdbc" % "2.0.4.RELEASE",
    )
  )

lazy val useCase = (project in file("usecase"))
  .dependsOn(adapters)
  .settings(
    name := "UseCase",
  )

lazy val eventBus = (project in file("eventbus"))
  .dependsOn(useCase)
  .settings(
    name := "EventBus",
  )

lazy val restApi = (project in file("restapi"))
  .dependsOn(relationalPersistence, eventBus)
  .settings(
    name := "RestApi",
    libraryDependencies ++= Seq(
      "org.springframework" % "spring-context" % "5.2.9.RELEASE",
      "com.github.finagle" %% "finchx-core" % "0.32.1",
      "com.github.finagle" %% "finchx-circe" % "0.32.1",
      "io.circe" %% "circe-generic-extras" % "0.13.0",
    )
  )

lazy val phanes = (project in file("."))
  .aggregate(config, adapters, domain, relationalPersistence, useCase, eventBus, restApi)
  .settings(
    name := "Phanes"
  )