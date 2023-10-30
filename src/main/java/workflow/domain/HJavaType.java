package workflow.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.*;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static org.flowable.bpmn.model.ImplementationType.IMPLEMENTATION_TYPE_CLASS;

@RequiredArgsConstructor
@Getter
public enum HJavaType {
  START_EVENT("StartEvent", task -> {
    StartEvent element = new StartEvent();
    return element;
  }),
  END_EVENT("EndEvent", task -> {
    EndEvent element = new EndEvent();
    return element;
  }),
  USER_TASK("UserTask", task -> {
    UserTask element = new UserTask();
    Map<String, Object> taskVariable = task.getTaskVariables();
    if (Objects.nonNull(taskVariable) && Objects.nonNull(taskVariable.get("assignee"))) {
      element.setAssignee(taskVariable.get("assignee").toString());
    }
    return element;
  }),
  PARALLEL_GATEWAY("ParallelGateway", task -> {
    ParallelGateway element = new ParallelGateway();
    return element;
  }),
  EXCLUSIVE_GATEWAY("ExclusiveGateway", task -> {
    ExclusiveGateway element = new ExclusiveGateway();
    return element;
  }),
  SERVICE_TASK("ServiceTask", task -> {
    ServiceTask element = new ServiceTask();

    // required non-null implementation, implementationType
    Map<String, Object> flowableVariable = task.getFlowableVariables();
    element.setImplementationType(flowableVariable.getOrDefault("implementationType", IMPLEMENTATION_TYPE_CLASS).toString());
    element.setImplementation(flowableVariable.get("implementation").toString());
    return element;
  }),

  ;
  private final String code;
  public final Function<HTask, FlowElement> apply;

  public FlowElement toFlowElement(HTask task) {
    return apply.andThen(flowElement -> {
      flowElement.setId(task.getId());
      flowElement.setName(task.getName());
      return flowElement;
    }).apply(task);
  }
}
