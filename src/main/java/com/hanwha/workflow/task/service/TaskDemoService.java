package com.hanwha.workflow.task.service;

import com.hanwha.workflow.task.dto.TaskRetrieveDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskDemoService {

  private final ProcessEngine processEngine;

  // get tasks
  public List<Map<String, Object>> getTasks(List<Task> taskList) {
    List<Map<String, Object>> list = new ArrayList<>();
    for (Task task : taskList) {
      Map<String, Object> transform = transform(task);
      list.add(transform);
    }
    return list;
  }


  // get task by assignee
  public List<Map<String, Object>> getTaskAssignee(String assignee) {
    return getTasks(processEngine.getTaskService()
        .createTaskQuery()
        .taskAssignee(assignee)
        .list());
  }


  // get task by groupCandidate
  public List<Map<String, Object>> getTaskGroupCandidate(String groupCandidate) {
    return getTasks(processEngine.getTaskService()
        .createTaskQuery()
        .taskCandidateGroup(groupCandidate)
        .list());
  }

  // changed assignee
  public List<Map<String, Object>> changeTaskAssignee(String assignee, String taskIndex) {
    processEngine.getTaskService().setAssignee(taskIndex, assignee);

    return getTasks(processEngine.getTaskService()
        .createTaskQuery()
        .taskAssignee(assignee).list()
    );
  }

  // complete task by task id
  public List<Map<String, Object>> completeTask(String taskIndex, boolean approved) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("approved", approved);

    TaskService taskService = processEngine.getTaskService();
    taskService.complete(taskIndex, variables);

    return getTasks(processEngine.getTaskService().createTaskQuery().list());
  }

  // get variables of task
  private static Map<String, Object> transform(Task task) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", task.getId());
    map.put("name", task.getName());
    map.put("assignee", task.getAssignee());
    map.put("processInstanceId", task.getProcessInstanceId());
    map.put("processDefinitionId", task.getProcessDefinitionId());
    map.put("processVariables", task.getProcessVariables());
    map.put("taskLocalVariables", task.getTaskLocalVariables());
    map.put("createTime", task.getCreateTime());
    map.put("description", task.getDescription());
    map.put("category", task.getCategory());
    map.put("tenantId", task.getTenantId());
    map.put("caseVariables", task.getCaseVariables());
    map.put("formKey", task.getFormKey());
    return map;
  }

  public Map<String, Object> getTaskVariables(String taskIndex) {
    return processEngine.getTaskService().getVariables(taskIndex);
  }

  public List<Map<String, Object>> getTasks(TaskRetrieveDto dto) {
    if (Objects.isNull(dto)) {
      return getTasks(processEngine.getTaskService()
          .createTaskQuery()
          .list());
    }

    return getTasks(processEngine.getTaskService()
        .createTaskQuery()
        .taskCandidateOrAssigned(dto.getValue())
        .list());
  }

}
