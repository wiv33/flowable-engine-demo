package com.hanwha.workflow.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParallelDto {

  private List<String> parallelAssignee;
  private String finalAssignee;
}