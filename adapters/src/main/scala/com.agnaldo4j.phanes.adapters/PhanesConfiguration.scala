package com.agnaldo4j.phanes.adapters

import com.typesafe.config.Config

trait PhanesConfiguration {
  def persistence(): Config
}
