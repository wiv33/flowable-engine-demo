package workflow.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class HTask {
  private String id; // task id // 해당 값으로 Point를 지정.
  private HJavaType javaType; // task type
  private String name; // task 이름
  private List<HFlowElement> addresses; // process 생성 중 SequenceFlow를 생성할 때 사용
  private Map<String, Object> flowableVariables; // process 생성 중 SequenceFlow를 만들 때 사용.
  private Map<String, Object> taskVariables; // process 생성 중 point를 만들 때 사용

  // responsible task
  // 1. 자신의 Type을 기준으로 Point, SequenceFlow를 만들 책임이 있다.

  public List<SequenceFlow> incomingToSequenceFlow() {
    System.out.println("############ start : javaType = " + javaType);
    return this.addresses.stream()
        .map(s -> {
          System.out.println(s.toString());
          SequenceFlow flow = new SequenceFlow(s.getSource(), s.getTarget());
          flow.setConditionExpression(s.getExpression()); // 해당 조건에 따라 분기를 타기도 함.
          return flow;
        }).collect(Collectors.toList());
  }

  public FlowElement makeFlow() {
    return javaType.toFlowElement(this);
  }
}
