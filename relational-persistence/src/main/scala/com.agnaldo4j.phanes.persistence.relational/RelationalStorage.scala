package com.agnaldo4j.phanes.persistence.relational

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.{Domain, Event}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RelationalStorage(
    @Autowired val eventsRepository: EventsRepository
) extends Storage {

  override def log(event: Event.Event): Unit = {
    val entity = EventEntity(
      id = "0cfbd177-bb27-4442-88dd-f1eca1872f10",
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
