package com.hanwha.workflow.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    System.out.println("############ Make flow : javaType = " + javaType);
    // 해당 조건에 따라 분기를 타기도 함.
    List<SequenceFlow> list = new ArrayList<>();
    for (HFlowElement address : this.addresses) {
      System.out.println(address.toString());
      SequenceFlow flow = new SequenceFlow(address.getSource(), address.getTarget());
      flow.setConditionExpression(address.getConditionExpression()); // 해당 조건에 따라 분기를 타기도 함.
      SequenceFlow apply = flow;
      list.add(apply);
    }
    return list;
  }

  public FlowElement makeFlow() {
    return javaType.toFlowElement(this);
  }
}
