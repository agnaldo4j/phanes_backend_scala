package com.agnaldo4j.phanes.persistence.relational

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
trait EventsRepository extends CrudRepository[EventEntity, String] {
  def findAllByName(name: String): java.util.List[EventEntity]
}
