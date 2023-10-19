package com.example.flowableenginedemo;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessEngineConfig {

  @Bean
  ProcessEngine processEngine() {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
        // jdbc postgresql
        .setJdbcUrl("jdbc:postgresql://psawesome.xyz:31907/flowable")
        .setJdbcUsername("postgres")
        .setJdbcPassword("powerful090")
        .setDatabaseSchema("public")
        .setJdbcDriver("org.postgresql.Driver")
        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    ProcessEngine processEngine = cfg.buildProcessEngine();

    System.out.println("init processEngine = " + processEngine.getName());

    return processEngine;
  }
}
