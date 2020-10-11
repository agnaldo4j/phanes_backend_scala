package com.agnaldo4j.phanes.config

import com.typesafe.config.{Config, ConfigFactory}

class ApplicationConfig {
  def load(): Config = ConfigFactory.defaultApplication()
}
