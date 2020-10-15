package com.agnaldo4j.phanes.persistence.relational.storage

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.Domain
import com.agnaldo4j.phanes.domain.StorableEvent.StorableEvent
import com.agnaldo4j.phanes.persistence.relational.entity.EventEntity
import com.agnaldo4j.phanes.persistence.relational.repository.EventsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RelationalStorage(
    @Autowired val eventsRepository: EventsRepository
) extends Storage {

  override def log(event: StorableEvent): Unit = {
    val entity = EventEntity(
      name = "teste"
    )
    eventsRepository.save(entity)
  }

  override def load(): Set[StorableEvent] = {
    val events = eventsRepository.findAll()
    println(events)
    Set.empty
  }

  override def loadSystem(): Option[Domain.System] = ???

  override def snapshot(system: Domain.System): Unit = ???
}
