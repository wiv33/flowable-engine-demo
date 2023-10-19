package com.example.flowableenginedemo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

public class ConsoleDemoApp {
public static void main(String[] args) throws InterruptedException {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
        // jdbc postgresql
        .setJdbcUrl("jdbc:postgresql://psawesome.xyz:31907/flowable")
        .setJdbcUsername("postgres")
        .setJdbcPassword("powerful090")
        .setDatabaseSchema("public")
        .setJdbcDriver("org.postgresql.Driver")
        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    ProcessEngine processEngine = cfg.buildProcessEngine();

    System.out.println("processEngine = " + processEngine.getName());

    // deploy the process definition
    RepositoryService repositoryService = processEngine.getRepositoryService();
    Deployment deployment = repositoryService.createDeployment()
        .addClasspathResource("bpmns/holiday-request.bpmn20.xml")
        .deploy();

    // verify that the process definition is deployed
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
        .deploymentId(deployment.getId())
        .singleResult();

    System.out.println("Found process definition : " + processDefinition.getName());

    Thread.sleep(1_000);

    // start a process instance
    Scanner scanner = new Scanner(System.in);

    System.out.println("Who are you?");
    String employee = "ps"; // scanner.nextLine();

    System.out.println("How many holidays do you want to request?");
    Integer nrOfHolidays = 7; // Integer.valueOf(scanner.nextLine());

    System.out.println("Why do you need them?");

    String description = "Family time"; // scanner.nextLine();

    RuntimeService runtimeService = processEngine.getRuntimeService();

    Map<String, Object> variables = new HashMap<>();
    variables.put("employee", employee);
    variables.put("nrOfHolidays", nrOfHolidays);
    variables.put("description", description);
    ProcessInstance processInstance =
        runtimeService.startProcessInstanceByKey("holidayRequest", variables);

    processInstance.getProcessVariables().forEach((k, v) -> {
      System.out.println(k + " : " + v);
    });

    TaskService taskService = processEngine.getTaskService();
    List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();

    tasksInfo(tasks);

    System.out.println("you can change the assignee.");
    String changeAssignee;
    // while
    List<String> idList = Arrays.asList("Lia", "kito", "ps", "moto", "lambda");
    do {

      // which task would you like to change the assignee?
      // 1
      System.out.println("which would you like to change manager or assignee? (manager/assignee)");
      String managerOrAssignee = scanner.nextLine();
      if (managerOrAssignee.matches("manager")) {
        tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        taskService.createTaskQuery().list();
        tasksInfo(tasks);
      } else {
        // who would you like to change the assignee to?
        System.out.println("who would you like to change the assignee to?");
        System.out.println("user list: " + idList);
        String targetTask = scanner.nextLine();

        tasks = taskService.createTaskQuery().taskAssignee(targetTask).list();
        tasksInfo(tasks);
      }

      System.out.println("which task would you like to change the assignee?");
      int taskIndex = Integer.parseInt(scanner.nextLine());
      Task task = tasks.get(taskIndex - 1);
      System.out.println("who would you like to change the assignee to?");
      System.out.println("user list: " + idList);
      String assignee = scanner.nextLine();
      while (!idList.contains(assignee)) {
        System.out.println("user list: " + idList);
        System.out.println("please choose user from list");
        assignee = scanner.nextLine();
      }

      taskService.setAssignee(task.getId(), assignee);

      System.out.println("task assignee changed to " + assignee);
      List<Task> assigneeTask = taskService.createTaskQuery().taskAssignee(assignee).list();
      tasksInfo(assigneeTask);

      tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
      tasksInfo(tasks);

      System.out.println("would you like to change the assignee?");
      changeAssignee = scanner.nextLine();
    } while (changeAssignee.equalsIgnoreCase("y"));

    System.out.println("Which task would you like to complete?");
    int taskIndex = Integer.parseInt(scanner.nextLine());
    Task task = tasks.get(taskIndex - 1);
    Map<String, Object> processVariables = taskService.getVariables(task.getId());
    System.out.println(processVariables.get("employee") + " wants " +
        processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

    boolean approved = scanner.nextLine().equalsIgnoreCase("y");
    variables = new HashMap<>();
    variables.put("approved", approved);
    taskService.complete(task.getId(), variables);

    // History
    HistoryService historyService = processEngine.getHistoryService();
    List<HistoricActivityInstance> activities =
        historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(processInstance.getId())
            .finished()
            .orderByHistoricActivityInstanceEndTime().asc()
            .list();

    for (HistoricActivityInstance activity : activities) {
      System.out.println(activity.getActivityId() + " took "
          + activity.getDurationInMillis() + " milliseconds");
    }

  }

  private static void tasksInfo(List<Task> tasks) {
    for (int i = 0; i < tasks.size(); i++) {
      System.out.printf("%d ) Task available: %s\n", i + 1, tasks.get(i).getName());

      System.out.println("assignee : " + tasks.get(i).getAssignee());
      System.out.println("owner : " + tasks.get(i).getOwner());
      System.out.println("variables : " + tasks.get(i).getProcessVariables());
      System.out.println("local variables : " + tasks.get(i).getTaskLocalVariables());
      System.out.println("task definition key : " + tasks.get(i).getTaskDefinitionKey());
      System.out.println("task definition id : " + tasks.get(i).getTaskDefinitionId());
      System.out.println("======================================");
    }
  }

}
