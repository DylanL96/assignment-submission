package com.example.backend.domain;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="comments")
public class Comment {
  @Id // indicates it is PK
  @GeneratedValue(strategy = GenerationType.IDENTITY) // use persistence provider(MySQL)to assign PK to each row of data
  private Long id;
  private ZonedDateTime createdDate;
  @ManyToOne 
  @JoinColumn(name = "user_id")
  private User createdBy; // what User does this comment belong to?
  @Column(columnDefinition = "TEXT")
  private String text;
  @ManyToOne // one assignment has many comments and any one comment is tied back to only one assignment
  @JsonIgnore
  private Assignment assignment; // what assignment does this comment belong to?
  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }
  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }
  public User getCreatedBy() {
    return createdBy;
  }
  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  public Assignment getAssignment() {
    return assignment;
  }
  public void setAssignment(Assignment assignment) {
    this.assignment = assignment;
  }

  
}
