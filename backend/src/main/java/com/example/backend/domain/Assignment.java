package com.example.backend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity // This annotation will create the table in MySQL and call it "Assignment", which is based on class name
public class Assignment {
  
  @Id // This is important because we need a PK to identify each row in database
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Persistent Provider is MySQL which will assign PK for us
  private long id;
  private Integer number;
  private String status;
  private String githubUrl;
  private String branch;
  private String codeReviewVideoUrl;

  // annotation at the field level
  // This annotation allows us to map the FK column in the child entity mapping so that the child has an entity object reference to its parent entity.
  // This annotation is indicating **Many assignments to One user**
  // When JPA evaluates this, it will find and map the entire User object, rather than just the User_ID! 
  // This is really powerful because when we query for Assignment entities, we will have access to the User details
  @ManyToOne(optional = false) //user will not be aware of assignments. assignment only aware of user
  private User user;

  // Code reviewer is able to have multiple code reviews at once
  // By default, ManyToOne is TRUE which means that it can have NO code reviewer assigned to it
  // i.e, when a student first submits an assignment, there is no code reviewer
  @ManyToOne
  private User codeReviewer;

  public long getId() {
    return id;
  }
  
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public void setId(long id) {
    this.id = id;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getGithubUrl() {
    return githubUrl;
  }
  public void setGithubUrl(String githubUrl) {
    this.githubUrl = githubUrl;
  }
  public String getBranch() {
    return branch;
  }
  public void setBranch(String branch) {
    this.branch = branch;
  }
  public String getCodeReviewVideoUrl() {
    return codeReviewVideoUrl;
  }
  public void setCodeReviewVideoUrl(String codeReviewVideoUrl) {
    this.codeReviewVideoUrl = codeReviewVideoUrl;
  }

  public User getCodeReviewer() {
    return codeReviewer;
  }

  public void setCodeReviewer(User codeReviewer) {
    this.codeReviewer = codeReviewer;
  }

  
}
