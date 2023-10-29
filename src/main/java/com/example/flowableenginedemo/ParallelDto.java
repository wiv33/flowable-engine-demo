package com.example.flowableenginedemo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParallelDto {

  private List<String> parallelAssignee;
  private String finalAssignee;
}
