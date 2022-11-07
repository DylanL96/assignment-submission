package com.example.backend.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class CommentDto {
  private Long id;
  private Long assignmentId;
  private String text;
  private String user;
  private ZonedDateTime createdDate;

  
  
  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }
  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Long getAssignmentId() {
    return assignmentId;
  }
  public void setAssignmentId(Long assignmentId) {
    this.assignmentId = assignmentId;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  public String getUser() {
    return user;
  }
  public void setUser(String user) {
    this.user = user;
  }
  @Override
  public String toString() {
    return "CommentDto [assignmentId=" + assignmentId + ", createdDate=" + createdDate + ", id=" + id + ", text=" + text
        + ", user=" + user + "]";
  }


  
}
