package com.hanwha.workflow.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HFlowElement {
  private String source;
  private String target;
  private String conditionExpression;

  @Override
  public String toString() {
    return String.format("{%s -> %s : expression: [%s]}", source, target, conditionExpression);
  }


}
