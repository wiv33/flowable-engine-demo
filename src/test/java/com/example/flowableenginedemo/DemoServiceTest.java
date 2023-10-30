package com.example.flowableenginedemo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoServiceTest {

  @Autowired
  ProcessEngine processEngine;

  StringBuilder sb;

  @Autowired
  DemoService demoService;

  @BeforeEach
  void setUp() throws FileNotFoundException {
    String source = "/Users/auto/github/flowable-examples/demo/demo-introducing-machine-learning/flowable-engine-demo/src/main/resources/bpmns/holiday-request.bpmn20.xml";

    String s;
    sb = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(source))) {
      while ((s = br.readLine()) != null) {
        sb.append(s);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testDeployDeployment() {

    String processName = "HolidayRequest";
    Deployment deploy = processEngine.getRepositoryService()
        .createDeployment()
        .name(processName)
        .key(processName)
        .category("http://www.flowable.org/processdef")
//        .addClasspathResource("bpmns/" + file.getName())
//        .addInputStream(processName, Files.newInputStream(file.toPath()))
//        .addBytes(processName, Files.readAllBytes(file.toPath()))
        .addString(processName, sb.toString() + ".bpmn20.xml")
        .deploy();

    System.out.println("deploy = " + deploy.getId());
    System.out.println("deploy = " + deploy.getResources());

    List<Deployment> list = processEngine.getRepositoryService()
        .createDeploymentQuery()
        .orderByDeploymentId()
        .asc().list()
        .stream().filter(s -> Integer.parseInt(s.getId()) > 100000)
        .collect(Collectors.toList());

    list.forEach(System.out::println);
  }
}