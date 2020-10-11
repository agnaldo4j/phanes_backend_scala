package com.agnaldo4j.phanes.persistence.relational.entity

import java.time.LocalDateTime
import java.util.UUID

import org.springframework.data.annotation.{CreatedDate, Id, Transient}
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table

@Table("events")
case class EventEntity(
    var id: String = UUID.randomUUID().toString,
    var created: LocalDateTime = null,
    var name: String
) extends Persistable[String] {
  @Id
  override def getId: String = id

  @CreatedDate
  def getCreated: LocalDateTime = created

  @Transient
  override def isNew: Boolean = true
}
