package com.agnaldo4j.phanes.persistence.quill

import com.agnaldo4j.phanes.adapters.Storage
import com.agnaldo4j.phanes.domain.Domain
import com.agnaldo4j.phanes.domain.StorableEvent.{
  AddOrganization,
  StorableEvent
}
import com.typesafe.config.Config

object QuillStorage {
  def apply(persistenceConfig: Config): QuillStorage = new QuillStorage(persistenceConfig)
}

class QuillStorage(override val persistenceConfig: Config)
    extends Storage
    with QuillPersistence {

  override def log(event: StorableEvent): Unit = {
    import ctx._

    val entity = EventEntity(
      name = "teste"
    )
    val a = quote {
      events
        .insert(lift(entity))
        .onConflictUpdate(_.id)((t, _) => t.name -> t.name)
    }

    ctx.run(a)
  }

  override def load(): List[StorableEvent] = {
    import ctx._

    val query = quote { events }

    ctx
      .run(query)
      .map { event =>
        AddOrganization(event.name)
      }
  }

  override def loadSystem(): Option[Domain.System] = ???

  override def snapshot(system: Domain.System): Unit = ???
}
