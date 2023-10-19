package com.example.flowableenginedemo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@RequiredArgsConstructor
//@Service
public class DemoHandler {

  private final ProcessEngine processEngine;
  private final List<String> idList = Arrays.asList("Lia", "kito", "ps", "moto", "lambda");

  public ServerResponse deployHoliday(ServerRequest request) {
    Deployment deploy = processEngine.getRepositoryService()
        .createDeployment()
        .addClasspathResource("bpmns/holiday-request.bpmn20.xml")
        .deploy();

    return ServerResponse.ok()
        .body(deploy, new ParameterizedTypeReference<Deployment>() {
        })
        ;
  }

  public ServerResponse changeTaskAssignee(ServerRequest serverRequest) {
    String taskIndex = serverRequest.pathVariable("taskIndex");
    String assignee = serverRequest.pathVariable("assignee");

    processEngine.getTaskService().setAssignee(taskIndex, assignee);

    return ServerResponse.ok()
        .body(processEngine.getTaskService().createTaskQuery()
            .taskAssignee(assignee).list());
  }

  public ServerResponse completeTask(ServerRequest serverRequest) {
    String taskIndex = serverRequest.pathVariable("taskIndex");
    String assignee = serverRequest.pathVariable("assignee");

    HashMap<String, Object> map = new HashMap<>();
    map.put("employee", assignee);
    processEngine.getTaskService().complete(taskIndex, map);

    return ServerResponse.ok()
        .body(processEngine.getTaskService().createTaskQuery().list());
  }

  public ServerResponse getTasks(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .body(processEngine.getTaskService().createTaskQuery().list());
  }

  public ServerResponse getTaskAssignee(ServerRequest serverRequest) {
    String assignee = serverRequest.pathVariable("assignee");

    return ServerResponse.ok()
        .body(processEngine.getTaskService().createTaskQuery()
            .taskAssignee(assignee).list());
  }

  public ServerResponse getTaskVariables(ServerRequest serverRequest) {
    String taskIndex = serverRequest.pathVariable("taskIndex");

    return ServerResponse.ok()
        .body(processEngine.getTaskService().getVariables(taskIndex));
  }

  public ServerResponse getProcessHistory(ServerRequest serverRequest) {
    String processId = serverRequest.pathVariable("processId");

    return ServerResponse.ok()
        .body(processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processInstanceId(processId)
            .list());
  }
}
