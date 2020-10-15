package com.agnaldo4j.phanes.eventbus

import com.agnaldo4j.phanes.domain.Domain.Id

object QueryableEvent {
  trait QueryableEvent

  case class GetOrganizationByName(name: String) extends QueryableEvent

  case class GetOrganizationById(id: Id) extends QueryableEvent
}
