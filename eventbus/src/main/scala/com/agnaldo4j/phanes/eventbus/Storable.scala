package com.agnaldo4j.phanes.eventbus

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.Domain.System
import com.agnaldo4j.phanes.domain.StorableEvent.StorableEvent

trait Storable {
  var system: System
  val storage: Storage

  def reload() {
    reloadSystem()
    reloadEvents()
  }

  def snapshot() {
    storage.snapshot(system)
  }

  def apply(event: StorableEvent)

  private def reloadSystem() {
    system = storage.loadSystem() match {
      case Some(loadedSystem) => loadedSystem
      case None               => System()
    }
  }

  private def reloadEvents() {
    val events = storage.load()
    events.foreach(apply)
  }
}
