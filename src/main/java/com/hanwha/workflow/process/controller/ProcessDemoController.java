package com.hanwha.workflow.process.controller;

import com.hanwha.workflow.domain.HProcess;
import com.hanwha.workflow.domain.ParallelDto;
import com.hanwha.workflow.process.service.DynamicDeployService;
import com.hanwha.workflow.process.service.ProcessDemoService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/process")
public class ProcessDemoController {

  private final ProcessDemoService processDemoService;
  private final DynamicDeployService dynamicDeployService;

  // deploy by holiday.bpmn
  @PostMapping("/holiday")
  public Deployment deployHoliday() {
    return processDemoService.deployHoliday();
  }

  // deploy by string
  @PostMapping(value = "/{processName}", consumes = MediaType.APPLICATION_XML_VALUE)
  public Deployment deployProcess(@PathVariable String processName,
                                  @RequestBody String content) {
    log.debug("request process name: {}, content : {}", processName, content);
    return processDemoService.deployProcess(processName, content);
  }

  // deploy by Java Component
  @PostMapping(value = "/{processName}/dynamic")
  public Deployment dynamicDeployProcess(@PathVariable String processName,
                                         @RequestBody ParallelDto parallelDto) {
    log.debug("request process name: {}, assignee: {}", processName, parallelDto);
    return dynamicDeployService.dynamicHoliday2(processName, parallelDto);
  }

  // deploy process made from json
  @PostMapping("/dynamic")
  public Deployment deployDynamicProcessMadeFromJson(@RequestBody HProcess process) {
    return dynamicDeployService.processMadeFromJson(process);
  }

  // started process to holiday
  @PostMapping("/{processDefKey}/assignee/{assignee}")
  public String startProcess(@PathVariable String processDefKey,
                             @PathVariable String assignee,
                             @RequestParam String holidays,
                             @RequestParam String description) {
    return processDemoService.startProcess(processDefKey, assignee, holidays, description);
  }

  // started process to article
  @PostMapping("/{processDefKey}")
  public String startProcessByParam(@PathVariable String processDefKey,
                                    @RequestParam Map<String, Object> params) {
    return processDemoService.startProcess(processDefKey, params);
  }

  // get process history
  @GetMapping("/{processId}")
  public List<HistoricActivityInstance> getProcessHistory(@PathVariable String processId) {
    return processDemoService.getProcessHistory(processId);
  }

}
