package workflow.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HServiceType {
  CLASS("class"),
  EXPRESSION("expression"),
  DELEGATE_EXPRESSION("delegateExpression"),
  DMN("dmn"),
  SHELL("shell"),
  MULE("mule"),
  CAMEL("camel"),
  HTTP("http"),
  ;

  private final String code;
}
