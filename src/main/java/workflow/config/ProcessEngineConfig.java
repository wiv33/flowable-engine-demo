package workflow.config;

import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProcessEngineConfig {

  @Value("${spring.datasource.url}")
  private String url;
  @Value("${spring.datasource.driverClassName}")
  private String driverClassName;
  @Value("${spring.datasource.username}")
  private String username;
  @Value("${spring.datasource.password}")
  private String password;


  @Bean(name = "processEngine")
  @Profile("local")
  ProcessEngine processH2Engine() {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
        // jdbc h2
        .setJdbcUrl(url)
        .setJdbcUsername(username)
        .setJdbcPassword(password)
        .setJdbcDriver(driverClassName)
        .setDatabaseSchemaUpdate(AbstractEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    return cfg.buildProcessEngine();
  }


  @Bean(name = "processEngine")
  @Profile("post")
  ProcessEngine processPostgresEngine() {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
        // jdbc postgresql
        .setJdbcUrl(url)
        .setJdbcUsername(username)
        .setJdbcPassword(password)
        .setJdbcDriver(driverClassName)
        .setDatabaseSchema("public")
        .setDatabaseSchemaUpdate(AbstractEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    return cfg.buildProcessEngine();
  }
}
