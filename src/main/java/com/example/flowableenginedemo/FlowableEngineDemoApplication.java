package com.example.flowableenginedemo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FlowableEngineDemoApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext run = SpringApplication.run(FlowableEngineDemoApplication.class,
        args);

    run.start();
  }

}
