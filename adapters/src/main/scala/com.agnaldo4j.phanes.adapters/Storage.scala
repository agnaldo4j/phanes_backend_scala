package com.agnaldo4j.phanes.adapters

import com.agnaldo4j.phanes.domain.Domain.System
import com.agnaldo4j.phanes.domain.Event.Event

trait Storage {
  def log(event: Event)
  def load(): Set[Event]
  def loadSystem(): Option[System]
  def snapshot(system: System)
}
