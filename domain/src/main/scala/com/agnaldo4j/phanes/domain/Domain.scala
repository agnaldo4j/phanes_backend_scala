package com.agnaldo4j.phanes.domain

import java.time.{Instant}
import java.util.UUID

object Domain {
  type Id = String

  object System {
    def apply(): System = new System()
    def apply(initialState: Map[String, Organization]): System =
      new System(organizations = initialState)
  }

  trait Domain {
    val id: Id
    val created: Instant
    val updated: Option[Instant]
  }

  case class System(
      id: Id = UUID.randomUUID().toString,
      created: Instant = Instant.now(),
      updated: Option[Instant] = None,
      organizations: Map[Id, Organization] = Map.empty
  ) extends Domain

  case class Organization(
      id: Id = UUID.randomUUID().toString,
      created: Instant = Instant.now(),
      updated: Option[Instant] = None,
      name: String,
      values: List[Value] = List.empty,
      people: Map[Id, Person] = Map.empty
  ) extends Domain

  case class Value(
      id: Id = UUID.randomUUID().toString,
      created: Instant = Instant.now(),
      updated: Option[Instant] = None,
      name: String
  ) extends Domain

  case class Person(
      id: Id = UUID.randomUUID().toString,
      created: Instant = Instant.now(),
      updated: Option[Instant] = None,
      name: String,
      givenFeedback: Set[Feedback] = Set.empty,
      receivedFeedback: Set[Feedback] = Set.empty
  ) extends Domain

  case class Feedback(
      id: Id = UUID.randomUUID().toString,
      created: Instant = Instant.now(),
      updated: Option[Instant] = None,
      values: List[Value],
      from: Person,
      to: Person,
      date: Instant,
      description: String
  ) extends Domain

}
