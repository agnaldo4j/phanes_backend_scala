package com.agnaldo4j.phanes.config

import com.agnaldo4j.phanes.adapters.PhanesConfiguration
import com.typesafe.config.{Config, ConfigFactory}

object ApplicationPhanesConfig extends PhanesConfiguration {
  lazy val config: Config = ConfigFactory.defaultApplication()

  def persistence(): Config = config.getConfig("persistence").resolve()
}
