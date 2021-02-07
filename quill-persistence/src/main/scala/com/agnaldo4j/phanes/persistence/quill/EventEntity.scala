package com.agnaldo4j.phanes.persistence.quill

import java.time.Instant
import java.util.UUID

case class EventEntity(
    var id: String = UUID.randomUUID().toString,
    var created: Instant = Instant.now(),
    var name: String
)
