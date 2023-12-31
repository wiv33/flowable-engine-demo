package com.example.flowableenginedemo;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendRejectMail implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) {
    System.out.println("Sending a rejection mail to "
        + execution.getVariable("employee"));
  }
}
