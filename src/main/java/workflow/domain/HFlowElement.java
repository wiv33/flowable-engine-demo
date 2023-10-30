package workflow.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HFlowElement {
  private String source;
  private String target;
  private String expression;

  @Override
  public String toString() {
    return String.format("{%s -> %s : expression: [%s]}", source, target, expression);
  }


}
