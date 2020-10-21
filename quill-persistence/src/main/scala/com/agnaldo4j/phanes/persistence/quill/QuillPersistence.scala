package com.agnaldo4j.phanes.persistence.quill

import com.typesafe.config.Config
import io.getquill.{PostgresJdbcContext, SnakeCase}

trait QuillPersistence {
  val persistenceConfig: Config

  lazy val ctx = new PostgresJdbcContext(SnakeCase, persistenceConfig)

  val events = ctx.quote {
    ctx.querySchema[EventEntity]("events")
  }
}
