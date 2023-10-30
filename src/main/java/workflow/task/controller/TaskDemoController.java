package workflow.task.controller;

import com.hanwha.workflow.task.dto.TaskRetrieveDto;
import com.hanwha.workflow.task.service.TaskDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tasks")
public class TaskDemoController {

  private final TaskDemoService taskDemoService;

  @GetMapping
  public List<Map<String, Object>> getTasks(@RequestBody(required = false) TaskRetrieveDto dto) {
    return taskDemoService.getTasks(dto);
  }

  @GetMapping("/{taskId}/variables")
  public Map<String, Object> getTaskVariables(@PathVariable String taskId) {
    return taskDemoService.getTaskVariables(taskId);
  }

  @PutMapping("/{taskId}/assignee/{assignee}")
  public List<Map<String, Object>> changeTaskAssignee(@PathVariable String assignee,
                                                      @PathVariable String taskId) {
    return taskDemoService.changeTaskAssignee(assignee, taskId);
  }

  @PostMapping("/{taskId}")
  public List<Map<String, Object>> completeTask(@PathVariable String taskId,
                                                @RequestParam(required = false, defaultValue = "false") boolean approved) {
    return taskDemoService.completeTask(taskId, approved);
  }

  @GetMapping("/assignee/{assignee}")
  public List<Map<String, Object>> getTaskAssignee(@PathVariable String assignee) {
    return taskDemoService.getTaskAssignee(assignee);
  }

}
