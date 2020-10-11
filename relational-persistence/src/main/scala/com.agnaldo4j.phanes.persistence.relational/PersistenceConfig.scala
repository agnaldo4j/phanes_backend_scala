package com.agnaldo4j.phanes.persistence.relational

import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import javax.sql.DataSource
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.{AbstractJdbcConfiguration, EnableJdbcRepositories}

@Configuration
@EnableJdbcRepositories
class PersistenceConfig(val persistenceConfig: Config) extends
  AbstractJdbcConfiguration {

  import org.springframework.context.annotation.Bean
  import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
  import org.springframework.jdbc.datasource.DataSourceTransactionManager

  @Bean def dataSource: DataSource = {
    val url = s"""jdbc:${persistenceConfig.getString("url")}"""

    val config = new HikariConfig()
    config.setDriverClassName(persistenceConfig.getString("driver"))
    config.setJdbcUrl(url)
    config.setUsername(persistenceConfig.getString("username"))
    config.setPassword(persistenceConfig.getString("password"))
    config.setMaximumPoolSize(persistenceConfig.getInt("maxPoolSize"))
    config.addDataSourceProperty("cachePrepStmts", "true")
    config.addDataSourceProperty("prepStmtCacheSize", "250")
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

    new HikariDataSource(config)
  }

  @Bean def namedParameterJdbcOperations(dataSource: DataSource) =
    new NamedParameterJdbcTemplate(dataSource)

  @Bean def transactionManager(dataSource: DataSource) =
    new DataSourceTransactionManager(dataSource)
}
