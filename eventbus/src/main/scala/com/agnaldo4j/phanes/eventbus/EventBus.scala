package com.agnaldo4j.phanes.eventbus

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.Domain.System
import com.agnaldo4j.phanes.domain.Event._

class EventBus(val storage: Storage)
    extends Storable
    with Queryable
    with Changeable {
  override var system: System = System()

  override def apply(event: Event): Unit = execute(event)
}
