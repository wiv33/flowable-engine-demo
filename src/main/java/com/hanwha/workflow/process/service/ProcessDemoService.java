package com.hanwha.workflow.process.service;

import static com.hanwha.workflow.constants.BpmnConstant.CATEGORY;
import static com.hanwha.workflow.constants.BpmnConstant.POST_FIX;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessDemoService {

  private final ProcessEngine processEngine;

  public Deployment deployHoliday() {
    return processEngine.getRepositoryService().createDeployment()
        .addClasspathResource("bpmns/holiday-request.bpmn20.xml").deploy();
  }

  public Deployment deployProcess(String processName, String content) {
    return processEngine.getRepositoryService()
        .createDeployment()
        .name(processName)
        .key(processName)
//        .tenantId(TENANT_ID)
        .category(CATEGORY)
        .addString(String.format("%s.%s", processName, POST_FIX), content)
//        .addClasspathResource("bpmns/" + file.getName())
//        .addInputStream(processName, Files.newInputStream(file.toPath()))
//        .addBytes(processName, Files.readAllBytes(file.toPath()))
        .deploy();
  }


  public String startProcess(String processDefKey, String assignee, String holidays, String description) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("employee", assignee);
    variables.put("nrOfHolidays", holidays);
    variables.put("description", description);

/*
    if (StringUtils.isNumeric(processDefKey)) {
      return processEngine.getRuntimeService()
          .startProcessInstanceById(processDefKey, variables)
          .getName();
    }
*/

    ProcessInstance processInstance = processEngine.getRuntimeService()
        .startProcessInstanceByKey(processDefKey, variables);

    return processInstance.getName();
  }

  public String startProcess(String processDefKey, Map<String, Object> params) {
    ProcessInstance processInstance = processEngine.getRuntimeService()
        .startProcessInstanceByKey(processDefKey, params);
    return processInstance.getName();
  }

  public List<HistoricActivityInstance> getProcessHistory(String processId) {
    if (processId.equals("0")) {
      return processEngine.getHistoryService()
          .createHistoricActivityInstanceQuery()
          .list();
    }

    return processEngine.getHistoryService()
        .createHistoricActivityInstanceQuery()
        .processInstanceId(processId)
        .list();
  }

  public Deployment dynamicHoliday(String processName, String assignee) {
    StartEvent startEvent = new StartEvent();
    startEvent.setId("startEvent");
    SequenceFlow sequenceFlow = new SequenceFlow("startEvent", "approveTask");

    UserTask approveTask = new UserTask();
    approveTask.setId("approveTask");
    approveTask.setCandidateGroups(Collections.singletonList("managers"));

    ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
    exclusiveGateway.setId("decision");

    ServiceTask st = new ServiceTask();
    st.setId("externalSystemCall");
//    st.setType(ServiceTask.MAIL_TASK);
    st.setName("external System Call");

    ServiceTask st2 = new ServiceTask();
    st2.setId("sendRejectMail");
//    st2.setType(ServiceTask.MAIL_TASK);
    st2.setName("send rejection mail");

    EndEvent endEvent = new EndEvent();
    endEvent.setId("approveEnd");

    ServiceTask holidayApprovedTask = new ServiceTask();
    holidayApprovedTask.setId("holidayApprovedTask");
//    holidayApprovedTask.setType(SendRejectionMail.class.getName());


    SequenceFlow sequenceFlow2 = new SequenceFlow("decision", "externalSystemCall");
    SequenceFlow sequenceFlow3 = new SequenceFlow("decision", "sendRejectMail");
    SequenceFlow sequenceFlow4 = new SequenceFlow("externalSystemCall", "holidayApprovedTask");
    SequenceFlow sequenceFlow5 = new SequenceFlow("holidayApprovedTask", "approveEnd");

    BpmnModel bpmnModel = new BpmnModel();
    Process process = new Process();
    bpmnModel.addProcess(process);
    process.setId(processName);

    process.addFlowElement(startEvent);
    process.addFlowElement(approveTask);
    process.addFlowElement(sequenceFlow);
    process.addFlowElement(exclusiveGateway);
    process.addFlowElement(holidayApprovedTask);
    process.addFlowElement(sequenceFlow2);
    process.addFlowElement(sequenceFlow3);
    process.addFlowElement(sequenceFlow4);
    process.addFlowElement(sequenceFlow5);
    process.addFlowElement(st);
    process.addFlowElement(st2);
    process.addFlowElement(endEvent);

    RepositoryService repositoryService = processEngine.getRepositoryService();

    return repositoryService
        .createDeployment()
        .addBpmnModel(processName + POST_FIX, bpmnModel)
        .deploy();
  }
}
