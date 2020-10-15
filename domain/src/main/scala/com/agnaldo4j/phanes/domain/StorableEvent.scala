package com.agnaldo4j.phanes.domain

import com.agnaldo4j.phanes.domain.Domain.Id

object StorableEvent {

  trait StorableEvent

  case class AddOrganization(name: String) extends StorableEvent

  case class DeleteOrganization(id: Id) extends StorableEvent
}
