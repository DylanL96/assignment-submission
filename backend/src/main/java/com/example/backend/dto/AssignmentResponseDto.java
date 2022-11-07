package com.example.backend.dto;

import com.example.backend.domain.Assignment;
import com.example.backend.enums.AssignmentEnum;
import com.example.backend.enums.AssignmentStatusEnum;

public class AssignmentResponseDto {
  private Assignment assignment;
  // private List<AssignmentEnumDto> assignmentEnums = new ArrayList<>();
  private AssignmentEnum[] assignmentEnums = AssignmentEnum.values();
  private AssignmentStatusEnum[] statusEnums = AssignmentStatusEnum.values();

  public AssignmentResponseDto(Assignment assignment) {
    super();
    this.assignment = assignment;
  }
  public Assignment getAssignment() {
    return assignment;
  }
  public void setAssignment(Assignment assignment) {
    this.assignment = assignment;
  }
  public AssignmentEnum[] getAssignmentEnums() {
    return assignmentEnums;
  } 
  public AssignmentStatusEnum[] getStatusEnums() {
    return statusEnums;
  }

  
}
