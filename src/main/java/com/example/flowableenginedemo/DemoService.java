package com.example.flowableenginedemo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class DemoService {

  private final ProcessEngine processEngine;

  private static Map<String, Object> apply(Task s) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", s.getId());
    map.put("name", s.getName());
    map.put("assignee", s.getAssignee());
    map.put("processInstanceId", s.getProcessInstanceId());
    map.put("processDefinitionId", s.getProcessDefinitionId());
    map.put("processVariables", s.getProcessVariables());
    map.put("taskLocalVariables", s.getTaskLocalVariables());
    return map;
  }

  public Deployment deployHoliday() {
    return processEngine.getRepositoryService().createDeployment()
        .addClasspathResource("bpmns/holiday-request.bpmn20.xml").deploy();
  }


  public List<Map<String, Object>> getTasks() {
    return (processEngine.getTaskService().createTaskQuery().taskCandidateGroup("managers").list()
        .stream().map(DemoService::apply).collect(Collectors.toList()));
  }

  public List<Map<String, Object>> getTaskAssignee(String assignee) {
    return (processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list().stream()
        .map(DemoService::apply).collect(Collectors.toList()));
  }

  public List<Map<String, Object>> changeTaskAssignee(String assignee, String taskIndex) {
    processEngine.getTaskService().setAssignee(taskIndex, assignee);

    return (processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list().stream()
        .map(DemoService::apply).collect(Collectors.toList()));
  }

  public Map<String, Object> completeTask(String taskIndex) {
    return processEngine.getTaskService().createTaskQuery().list().stream().map(DemoService::apply)
        .collect(Collectors.toList()).get(Integer.parseInt(taskIndex));
  }

  public String getTaskVariables(String taskIndex) {
    return (processEngine.getTaskService().getVariables(taskIndex).toString());
  }

  public List<?> getProcessHistory(String processId) {
    return Collections.singletonList(
        (processEngine.getHistoryService().createHistoricActivityInstanceQuery()
            .processInstanceId(processId).list()));
  }
}
