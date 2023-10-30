package com.hanwha.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;

/**
 * Bpmn process 객체
 */
@Data
@NoArgsConstructor
@ToString
public class HProcess {
  public static final String POST_FIX = ".bpmn20.xml";
  private String processId;
  private String processName;

  private String category;
  private String key;
  private String tenantId;

  // more then...
  @JsonIgnore
  BpmnModel result = new BpmnModel();

  @JsonIgnore
  Process process = new Process();

  public HProcess(String processId, String processName, String category, String key, String tenantId, List<HTask> tasks) {
    this.processId = processId;
    this.processName = processName;
    this.tasks = tasks;
    this.category = category;
    this.key = key;
    this.tenantId = tenantId;
  }


  private List<HTask> tasks;

  // responsible process
  // 1. 프로세스 플로우를 만들 책임이 있다.


  public BpmnModel toBpmnModel() {
    process.setName(processName);
    process.setId(processId);

    result.addProcess(process);

    tasks.forEach(task -> process.addFlowElement(task.makeFlow()));
    tasks.forEach(task -> task.incomingToSequenceFlow().forEach(process::addFlowElement));

    return result;
  }

}
