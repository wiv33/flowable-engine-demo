package com.example.flowableenginedemo;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FlowableEngineDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlowableEngineDemoApplication.class, args);

  }

  @Slf4j
  static class IntroTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
      if (execution.hasVariable("intro")) {
        log.info("intro variable available with value: {}", execution.getVariable("intro"));
        execution.setVariable("variablePresent", true);
      } else {
        log.info("intro variable not available");
        execution.setVariable("variablePresent", false);
      }
    }
  }

}
