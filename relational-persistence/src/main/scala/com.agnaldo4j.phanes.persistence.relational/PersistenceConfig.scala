package com.agnaldo4j.phanes.persistence.relational

import com.agnaldo4j.phanes.adapters.PhanesConfiguration
import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.{
  AbstractJdbcConfiguration,
  EnableJdbcAuditing,
  EnableJdbcRepositories
}

@Configuration
@EnableJdbcRepositories
@EnableJdbcAuditing
class PersistenceConfig(
    @Autowired val phanesConfiguration: PhanesConfiguration
) extends AbstractJdbcConfiguration {

  import org.springframework.context.annotation.Bean
  import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
  import org.springframework.jdbc.datasource.DataSourceTransactionManager

  lazy val persistenceConfig: Config = phanesConfiguration.persistence()

  @Bean def dataSource: DataSource = {
    val config = new HikariConfig()
    config.setDriverClassName(persistenceConfig.getString("driver"))
    config.setJdbcUrl(s"""jdbc:${persistenceConfig.getString("url")}""")
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
