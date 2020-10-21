package com.agnaldo4j.phanes.adapters

import com.agnaldo4j.phanes.domain.Domain.System
import com.agnaldo4j.phanes.domain.StorableEvent.StorableEvent

trait Storage {
  def log(event: StorableEvent)
  def load(): List[StorableEvent]
  def loadSystem(): Option[System]
  def snapshot(system: System)
}
