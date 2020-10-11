package com.agnaldo4j.phanes.config

import com.agnaldo4j.phanes.adapters.PhanesConfiguration
import com.typesafe.config.{Config, ConfigFactory}
import org.springframework.stereotype.Component

@Component
class ApplicationPhanesConfig extends PhanesConfiguration {
  lazy val config: Config = ConfigFactory.defaultApplication()

  def persistence(): Config = config.getConfig("persistence").resolve()
}
