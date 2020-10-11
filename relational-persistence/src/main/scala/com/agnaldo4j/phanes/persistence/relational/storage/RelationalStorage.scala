package com.agnaldo4j.phanes.persistence.relational.storage

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.{Domain, Event}
import com.agnaldo4j.phanes.persistence.relational.entity.EventEntity
import com.agnaldo4j.phanes.persistence.relational.repository.EventsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RelationalStorage(
    @Autowired val eventsRepository: EventsRepository
) extends Storage {

  override def log(event: Event.Event): Unit = {
    val entity = EventEntity(
      name = "teste"
    )
    eventsRepository.save(entity)
  }

  override def load(): Set[Event.Event] = {
    val events = eventsRepository.findAll()
    println(events)
    Set.empty
  }

  override def loadSystem(): Option[Domain.System] = ???

  override def snapshot(system: Domain.System): Unit = ???
}
