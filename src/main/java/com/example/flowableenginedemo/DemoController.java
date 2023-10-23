package com.example.flowableenginedemo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
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
  @PostMapping("/process/holiday")
  public Deployment deployHoliday() {
    return demoService.deployHoliday();
  }

  @PostMapping(value = "/process/{processName}", consumes = MediaType.APPLICATION_XML_VALUE)
  public Deployment deployProcess(@PathVariable String processName,
                                  @RequestBody String content) {
    log.debug("request process name: {}, content : {}", processName, content);
    return demoService.deployProcess(processName, content);
  }

  @PostMapping(value = "/process/{processName}/dynamic")
  public Deployment dynamicDeployProcess(@PathVariable String processName) {
    log.debug("request process name: {}", processName);
    return demoService.dynamicHoliday(processName);
  }

  @PostMapping("/process/{processDefKey}/assignee/{assignee}")
  public String startProcess(@PathVariable String processDefKey,
                             @PathVariable String assignee,
                             @RequestParam String holidays,
                             @RequestParam String description) {
    return demoService.startProcess(processDefKey, assignee, holidays, description);
  }

  @PostMapping("/process/{processDefKey}")
  public String startProcessByParam(@PathVariable String processDefKey,
                                    @RequestParam Map<String, Object> params) {
    return demoService.startProcess(processDefKey, params);
  }

  @GetMapping("/process/{processId}")
  public List<HistoricActivityInstance> getProcessHistory(@PathVariable String processId) {
    return demoService.getProcessHistory(processId);
  }


  @GetMapping("/tasks/{groups}")
  public List<Map<String, Object>> getTasks(@PathVariable DemoService.Groups groups) {
    return demoService.getTasks(groups);
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
  public List<Map<String, Object>> completeTask(@PathVariable String taskIndex,
                                                @RequestParam(required = false, defaultValue = "false") boolean approved) {
    return demoService.completeTask(taskIndex, approved);
  }


  @GetMapping("/tasks/{taskIndex}/variables")
  public String getTaskVariables(@PathVariable String taskIndex) {
    return demoService.getTaskVariables(taskIndex);
  }


}
