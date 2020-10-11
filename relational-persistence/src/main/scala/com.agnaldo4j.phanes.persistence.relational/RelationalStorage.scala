package com.agnaldo4j.phanes.persistence.relational

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.{Domain, Event}

object RelationalStorage extends Storage {
  override def log(event: Event.Event): Unit = ???

  override def load(): Set[Event.Event] = ???

  override def loadSystem(): Option[Domain.System] = ???

  override def snapshot(system: Domain.System): Unit = ???
}
