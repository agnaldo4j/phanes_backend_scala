package com.agnaldo4j.phanes.domain

import java.time.LocalDateTime
import java.util.UUID

object Domain {
  type Id = String

  trait Domain {
    val id: Id = UUID.randomUUID().toString
    val created: LocalDateTime = LocalDateTime.now()
    val updated: Option[LocalDateTime] = None
  }

  case class System(
      organizations: Map[Id, Organization] = Map.empty
  ) extends Domain

  case class Organization(
      name: String,
      values: List[Value] = List.empty,
      people: Map[Id, Person] = Map.empty
  ) extends Domain

  case class Value(
      name: String
  ) extends Domain

  case class Person(
      name: String,
      givenFeedback: Set[Feedback] = Set.empty,
      receivedFeedback: Set[Feedback] = Set.empty
  ) extends Domain

  case class Feedback(
      values: List[Value],
      from: Person,
      to: Person,
      date: LocalDateTime,
      description: String
  ) extends Domain

}
