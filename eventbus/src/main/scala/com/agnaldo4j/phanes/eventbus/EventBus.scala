package com.agnaldo4j.phanes.eventbus

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.Domain.System
import com.agnaldo4j.phanes.domain.StorableEvent.StorableEvent
import com.agnaldo4j.phanes.eventbus.system.{SystemChangeable, SystemQueryable}

class EventBus(val storage: Storage)
    extends Storable
    with SystemQueryable
    with SystemChangeable {
  override var system: System = System()

  override def apply(event: StorableEvent): Unit = execute(event)
}
