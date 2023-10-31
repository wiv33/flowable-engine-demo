package com.hanwha.workflow.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParallelDto {

  private List<String> parallelAssignee;
  private String finalAssignee;
}