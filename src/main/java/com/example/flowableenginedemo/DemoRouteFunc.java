package com.example.flowableenginedemo;





import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RequestPredicates.POST;
import static org.springframework.web.servlet.function.RequestPredicates.PUT;
import static org.springframework.web.servlet.function.RouterFunctions.route;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;

//@RequiredArgsConstructor
//@Component
public class DemoRouteFunc {

  private DemoHandler demoHandler;


  @Bean
  RouterFunction<?> routes() {
    return route(GET("/deploy/holiday"), demoHandler::deployHoliday)
        .andRoute(GET("/tasks"), demoHandler::getTasks)
        .andRoute(GET("/tasks/assignee/{assignee}"), demoHandler::getTaskAssignee)
        .andRoute(PUT("/tasks/{taskIndex}/assignee/{assignee}"), demoHandler::changeTaskAssignee)
        .andRoute(POST("/tasks/{taskIndex}"), demoHandler::completeTask)
        .andRoute(GET("/tasks/{taskIndex}/variables"), demoHandler::getTaskVariables)
        .andRoute(GET("/process/{processId}"), demoHandler::getProcessHistory)
        ;
  }

}
