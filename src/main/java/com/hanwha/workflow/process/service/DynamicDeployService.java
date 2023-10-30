package com.hanwha.workflow.process.service;

import static com.hanwha.workflow.constants.BpmnConstant.POST_FIX;
import static org.flowable.bpmn.model.ImplementationType.IMPLEMENTATION_TYPE_CLASS;

import com.hanwha.workflow.domain.HProcess;
import com.hanwha.workflow.domain.ParallelDto;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.bpmn.model.ParallelGateway;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class DynamicDeployService {

  private final ProcessEngine processEngine;


  public Deployment processMadeFromJson(HProcess hProcess) {
    log.info("hProcess: {}", hProcess);
    BpmnModel bpmnModel = hProcess.toBpmnModel();

    return processEngine.getRepositoryService()
        .createDeployment()
        .tenantId(hProcess.getTenantId())
        .key(hProcess.getProcessId())
        .name(hProcess.getProcessName())
        .addBpmnModel(hProcess.getProcessId() + POST_FIX, bpmnModel)
        .deploy();
  }

  public Deployment dynamicHoliday2(String processName, ParallelDto parallelDto) {
    StartEvent startEvent = new StartEvent();
    startEvent.setId("startEvent");

    ParallelGateway parallelGateway = new ParallelGateway();
    parallelGateway.setId("fork");

    SequenceFlow fork1 = new SequenceFlow("startEvent", "fork");

    SequenceFlow sequenceParallel1 = new SequenceFlow("fork", "parallel1");
    SequenceFlow sequenceParallel2 = new SequenceFlow("fork", "parallel2");

    UserTask parallel1 = new UserTask();
    parallel1.setId("parallel1");
    parallel1.setAssignee(parallelDto.getParallelAssignee().get(0));

    SequenceFlow join1 = new SequenceFlow("parallel1", "join");

    UserTask parallel2 = new UserTask();
    parallel2.setId("parallel2");
    parallel2.setAssignee(parallelDto.getParallelAssignee().get(1));
    SequenceFlow join2 = new SequenceFlow("parallel2", "join");

    ParallelGateway join = new ParallelGateway();
    join.setId("join");

    UserTask approveTask = new UserTask();
    approveTask.setId("approveTask");

    SequenceFlow sequenceFlow = new SequenceFlow("join", "approveTask");

    ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
    exclusiveGateway.setId("decision");

    ServiceTask st = new ServiceTask();
    st.setId("externalSystemCall");
    st.setName("external System Call");
    st.setImplementationType(IMPLEMENTATION_TYPE_CLASS); // 수정: 클래스 대리자 사용
    st.setImplementation("com.hanwha.workflow.delegate.CallExternalSystemDelegate");

    ServiceTask st2 = new ServiceTask();
    st2.setId("sendRejectMail");
    st2.setName("send rejection mail");
    st2.setImplementationType(IMPLEMENTATION_TYPE_CLASS); // 수정: 클래스 구현 사용
    st2.setImplementation("com.hanwha.workflow.delegate.SendRejectMail");

    EndEvent endEvent = new EndEvent();
    endEvent.setId("approveEnd");

    UserTask holidayApprovedTask = new UserTask();
    holidayApprovedTask.setId("holidayApprovedTask");
    holidayApprovedTask.setAssignee(parallelDto.getFinalAssignee());

    SequenceFlow sequenceFlow2 = new SequenceFlow("decision", "externalSystemCall");
    SequenceFlow sequenceFlow3 = new SequenceFlow("decision", "sendRejectMail");
    SequenceFlow sequenceFlow4 = new SequenceFlow("externalSystemCall", "holidayApprovedTask");
    SequenceFlow sequenceFlow5 = new SequenceFlow("holidayApprovedTask", "approveEnd");
    SequenceFlow sequenceFlow6 = new SequenceFlow("sendRejectMail", "approveEnd");

    BpmnModel bpmnModel = new BpmnModel();
    Process process = new Process();
    bpmnModel.addProcess(process);
    process.setId(processName);

    process.addFlowElement(startEvent);
    process.addFlowElement(approveTask);
    process.addFlowElement(parallelGateway);
    process.addFlowElement(fork1);
    process.addFlowElement(parallel1);
    process.addFlowElement(parallel2);
    process.addFlowElement(sequenceParallel1);
    process.addFlowElement(sequenceParallel2);
    process.addFlowElement(join);
    process.addFlowElement(join1);
    process.addFlowElement(join2);
    process.addFlowElement(sequenceFlow);
    process.addFlowElement(exclusiveGateway);
    process.addFlowElement(holidayApprovedTask);
    process.addFlowElement(sequenceFlow2);
    process.addFlowElement(sequenceFlow3);
    process.addFlowElement(sequenceFlow4);
    process.addFlowElement(sequenceFlow5);
    process.addFlowElement(sequenceFlow6);
    process.addFlowElement(st);
    process.addFlowElement(st2);
    process.addFlowElement(endEvent);

    RepositoryService repositoryService = processEngine.getRepositoryService();

    return repositoryService
        .createDeployment()
        .addBpmnModel(processName + POST_FIX, bpmnModel)
        .deploy();
  }


  public Deployment dynamicHoliday(String processName, String assignee) {
    StartEvent startEvent = new StartEvent();
    startEvent.setId("startEvent");
    SequenceFlow sequenceFlow = new SequenceFlow("startEvent", "approveTask");

    UserTask approveTask = new UserTask();
    approveTask.setId("approveTask");
    approveTask.setCandidateGroups(Collections.singletonList("MANAGERS"));

    SequenceFlow flow = new SequenceFlow("approveTask", "decision");

    ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
    exclusiveGateway.setId("decision");

    ServiceTask st = new ServiceTask();
    st.setId("externalSystemCall");
    st.setName("external System Call");
    st.setImplementation("com.example.flowableenginedemo.CallExternalSystemDelegate");
//    st.setImplementationType("com.example.flowableenginedemo.CallExternalSystemDelegate");
    HashMap<String, List<ExtensionAttribute>> extension = new HashMap<>();
    ExtensionAttribute v = new ExtensionAttribute();
    v.setName("class");
    v.setValue("com.example.flowableenginedemo.CallExternalSystemDelegate");
    extension.put("class", Collections.singletonList(v));
    st.setAttributes(extension);

    ServiceTask st2 = new ServiceTask();
    st2.setId("sendRejectMail");
    st2.setName("send rejection mail");
    st2.setImplementation("com.example.flowableenginedemo.SendRejectMail");
//    st2.setImplementationType("com.example.flowableenginedemo.SendRejectMail");

    HashMap<String, List<ExtensionAttribute>> extension2 = new HashMap<>();
    ExtensionAttribute v2 = new ExtensionAttribute();
    v2.setName("class");
    v2.setValue("com.example.flowableenginedemo.SendRejectMail");
    extension2.put("class", Collections.singletonList(v2));
    st2.setAttributes(extension2);

    EndEvent endEvent = new EndEvent();
    endEvent.setId("approveEnd");

    UserTask holidayApprovedTask = new UserTask();
    holidayApprovedTask.setId("holidayApprovedTask");
    holidayApprovedTask.setAssignee(assignee);

    SequenceFlow sequenceFlow2 = new SequenceFlow("decision", "externalSystemCall");
    sequenceFlow2.setConditionExpression("${approved}");
    SequenceFlow sequenceFlow3 = new SequenceFlow("decision", "sendRejectMail");
    sequenceFlow3.setConditionExpression("${!approved}");
    SequenceFlow sequenceFlow4 = new SequenceFlow("externalSystemCall", "holidayApprovedTask");
    SequenceFlow sequenceFlow5 = new SequenceFlow("holidayApprovedTask", "approveEnd");
    SequenceFlow sequenceFlow6 = new SequenceFlow("sendRejectMail", "approveEnd");
    BpmnModel bpmnModel = new BpmnModel();

    Process process = new Process();
    bpmnModel.addProcess(process);

    process.setId(processName);
    process.setExecutable(true);
    process.setName("Holiday Request");

    process.addFlowElement(startEvent);
    process.addFlowElement(approveTask);
    process.addFlowElement(sequenceFlow);
    process.addFlowElement(flow);
    process.addFlowElement(exclusiveGateway);
    process.addFlowElement(holidayApprovedTask);
    process.addFlowElement(sequenceFlow2);
    process.addFlowElement(sequenceFlow3);
    process.addFlowElement(sequenceFlow4);
    process.addFlowElement(sequenceFlow5);
    process.addFlowElement(sequenceFlow6);
    process.addFlowElement(st);
    process.addFlowElement(st2);
    process.addFlowElement(endEvent);

    process.getFlowElements()
        .forEach(System.out::println);

    System.out.println(process.getDocumentation());

    RepositoryService repositoryService = processEngine.getRepositoryService();

    return repositoryService
        .createDeployment()
        .addBpmnModel(processName + "." + POST_FIX, bpmnModel)
//        .disableBpmnValidation()
//        .disableSchemaValidation()
        .deploy();
  }
}
