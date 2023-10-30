package workflow.task.dto;

public class ByGroupCandidateDto extends TaskRetrieveDto {
  public ByGroupCandidateDto(String value) {
    super(value, ByType.GROUPS_CANDIDATE);
  }

}
