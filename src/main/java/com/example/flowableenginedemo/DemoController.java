package com.example.flowableenginedemo;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.repository.Deployment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DemoController {

  private final DemoService demoService;
  // change to controller
  /*

  @Bean
  RouterFunction<?> routes() {
    return route(GET("/deploy/holiday"), demoHandler::deployHoliday)
        .andRoute(GET("/tasks"), demoHandler::getTasks)
        .andRoute(GET("/tasks/assignee/{assignee}"), demoHandler::getTaskAssignee)
        .andRoute(PUT("/tasks/{taskIndex}/assignee/{assignee}"), demoHandler::changeTaskAssignee)
        .andRoute(POST("/tasks/{taskIndex}"), demoHandler::completeTask)
        .andRoute(GET("/tasks/{taskIndex}/variables"), demoHandler::getTaskVariables)
        .andRoute(GET("/process/{processId}"), demoHandler::getProcessHistory)
        ;
  }
   */

  // route changes to controller

  // change to controller
  @PostMapping("/deploy/holiday")
  public Deployment deployHoliday() {
    return demoService.deployHoliday();
  }

  @PostMapping("/process/assignee/{assignee}")
  public String startProcess(@PathVariable String assignee,
      @RequestParam String holidays,
      @RequestParam String description) {
    return demoService.startProcess(assignee, holidays, description);
  }


  @GetMapping("/tasks")
  public List<Map<String, Object>> getTasks() {
    return demoService.getTasks();
  }

  @GetMapping("/tasks/assignee/{assignee}")
  public List<Map<String, Object>> getTaskAssignee(@PathVariable String assignee) {
    return demoService.getTaskAssignee(assignee);
  }

  @PutMapping("/tasks/{taskIndex}/assignee/{assignee}")
  public List<Map<String, Object>> changeTaskAssignee(@PathVariable String assignee,
      @PathVariable String taskIndex) {
    return demoService.changeTaskAssignee(assignee, taskIndex);
  }

  @PostMapping("/tasks/{taskIndex}")
  public List<Map<String, Object>> completeTask(@PathVariable String taskIndex
      , @RequestParam(required = false, defaultValue = "false") boolean approved) {
    return demoService.completeTask(taskIndex, approved);
  }


  @GetMapping("/tasks/{taskIndex}/variables")
  public String getTaskVariables(@PathVariable String taskIndex) {
    return demoService.getTaskVariables(taskIndex);
  }

  @GetMapping("/process/{processId}")
  public List<?> getProcessHistory(@PathVariable String processId) {
    return demoService.getProcessHistory(processId);
  }


}
