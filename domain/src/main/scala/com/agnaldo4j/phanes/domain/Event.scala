package com.agnaldo4j.phanes.domain

import com.agnaldo4j.phanes.domain.Domain.Id

object Event {
  trait Event

  case class AddOrganization(name: String) extends Event

  case class DeleteOrganization(id: Id) extends Event

  trait QueryEvent

  case class GetOrganizationByName(name: String) extends QueryEvent

  case class GetOrganizationById(id: Id) extends QueryEvent
}
