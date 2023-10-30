package workflow.task.dto;

public class ByAssigneeDto extends TaskRetrieveDto {
  public ByAssigneeDto(String value) {
    super(value, ByType.ASSIGNEE);
  }
}
