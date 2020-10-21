package com.agnaldo4j.phanes.persistence.quill

import java.time.LocalDateTime
import java.util.UUID

case class EventEntity(
    var id: String = UUID.randomUUID().toString,
    var created: LocalDateTime = LocalDateTime.now(),
    var name: String
)
