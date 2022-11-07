package com.example.backend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Authority implements GrantedAuthority {
  private static final long serialVersionUID = -6520888182797362903L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String authority;
  @ManyToOne() // indicates many Authority to one user
  private User user; //Implies one to many relationship. One user can have many authorities.

  public Authority(){};

  public Authority(String authority){
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return authority;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public void setAuthority(String authority) {
    this.authority = authority;
  }
  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }

  
}  
