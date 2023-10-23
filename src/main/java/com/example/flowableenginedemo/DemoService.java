package com.example.flowableenginedemo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class DemoService {

  private final ProcessEngine processEngine;
  private static final String POST_FIX = ".bpmn20.xml";

  private static Map<String, Object> apply(Task s) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", s.getId());
    map.put("name", s.getName());
    map.put("assignee", s.getAssignee());
    map.put("processInstanceId", s.getProcessInstanceId());
    map.put("processDefinitionId", s.getProcessDefinitionId());
    map.put("processVariables", s.getProcessVariables());
    map.put("taskLocalVariables", s.getTaskLocalVariables());
    map.put("createTime", s.getCreateTime());
    map.put("description", s.getDescription());
    return map;
  }

  public Deployment deployHoliday() {
    return processEngine.getRepositoryService().createDeployment()
        .addClasspathResource("bpmns/holiday-request.bpmn20.xml").deploy();
  }

  public Deployment deployProcess(String processName, String content) {
    return processEngine.getRepositoryService()
        .createDeployment()
        .name(processName)
        .key(processName)
        .tenantId("hanwha")
        .category("http://www.flowable.org/processdef")
//        .addClasspathResource("bpmns/" + file.getName())
//        .addInputStream(processName, Files.newInputStream(file.toPath()))
//        .addBytes(processName, Files.readAllBytes(file.toPath()))
        .addString(String.format("%s%s", processName, POST_FIX), content)
        .deploy();
  }


  public Deployment dynamicHoliday(String processName) {
    StartEvent startEvent = new StartEvent();
    startEvent.setId("startEvent");
    SequenceFlow sequenceFlow = new SequenceFlow("startEvent", "approveTask");

    ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
    exclusiveGateway.setId("decision");

    SequenceFlow sequenceFlow2 = new SequenceFlow("decision", "externalSystemCall");
    SequenceFlow sequenceFlow3 = new SequenceFlow("decision", "sendRejectionMail");
    SequenceFlow sequenceFlow4 = new SequenceFlow("externalSystemCall", "holidayApprovedTask");
    SequenceFlow sequenceFlow5 = new SequenceFlow("holidayApprovedTask", "approveEnd");

    EndEvent endEvent = new EndEvent();
    endEvent.setId("approveEnd");

    BpmnModel bpmnModel = new BpmnModel();
    org.flowable.bpmn.model.Process process = new Process();
    bpmnModel.addProcess(process);
    process.setId(processName);

    process.addFlowElement(startEvent);
    process.addFlowElement(sequenceFlow);
    process.addFlowElement(exclusiveGateway);
    process.addFlowElement(sequenceFlow2);
    process.addFlowElement(sequenceFlow3);
    process.addFlowElement(sequenceFlow4);
    process.addFlowElement(sequenceFlow5);
    process.addFlowElement(endEvent);

    RepositoryService repositoryService = processEngine.getRepositoryService();
    return repositoryService
        .createDeployment()
        .addBpmnModel(processName + POST_FIX, bpmnModel)
        .deploy();
  }

  public String startProcess(String processDefKey, String assignee, String holidays, String description) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("employee", assignee);
    variables.put("nrOfHolidays", holidays);
    variables.put("description", description);
    ProcessInstance processInstance = processEngine.getRuntimeService()
        .startProcessInstanceByKey(processDefKey, variables);

    return processInstance.getName();
  }

  public String startProcess(String processDefKey, Map<String, Object> params) {
    ProcessInstance processInstance = processEngine.getRuntimeService()
        .startProcessInstanceByKey(processDefKey, params);
    return processInstance.getName();
  }


  public List<Map<String, Object>> getTasks(Groups groups) {
    return (processEngine.getTaskService().createTaskQuery()
        .taskCandidateGroup(groups.name().toLowerCase(Locale.ROOT)).list()
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

  public List<Map<String, Object>> completeTask(String taskIndex, boolean approved) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("approved", approved);

    TaskService taskService = processEngine.getTaskService();
    taskService.complete(taskIndex, variables);

    return processEngine.getTaskService().createTaskQuery()
        .list()
        .stream()
        .map(DemoService::apply)
        .collect(Collectors.toList());
  }

  public String getTaskVariables(String taskIndex) {
    return (processEngine.getTaskService().getVariables(taskIndex).toString());
  }

  public List<HistoricActivityInstance> getProcessHistory(String processId) {
    return processEngine.getHistoryService()
        .createHistoricActivityInstanceQuery()
        .processInstanceId(processId)
        .list();
  }


  @Getter
  public enum Groups {
    MANAGERS, EDITORS
  }
}
