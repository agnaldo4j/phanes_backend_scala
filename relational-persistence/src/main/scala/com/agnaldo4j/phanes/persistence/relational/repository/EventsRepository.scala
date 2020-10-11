package com.agnaldo4j.phanes.persistence.relational.repository

import com.agnaldo4j.phanes.persistence.relational.entity.EventEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
trait EventsRepository extends CrudRepository[EventEntity, String] {
  def findAllByName(name: String): java.util.List[EventEntity]
}
