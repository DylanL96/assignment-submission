package com.example.backend.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// domain object which means it represents a database table
@Entity
@Table(name="users") // usually do not pluralize table but this is exception because User is a reserved word in SQL
public class User implements UserDetails { 
  // UserDetails comes from Spring Security and has abstract methods that have to be overridden
  // We are having User implementing UserDetails because in our UserDetailsServiceImpl class, we have public UserDetails which is expecting to
  // return a UserDetails object. So by having User implementing UserDetails, we can successfully return the UserDetails Object in the UserDetailsServiceImpl class

  @Id // This is important because we need a PK to identify each row in database
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; //id is typically datatype Long
  private LocalDate cohortStartDate;
  private String username;
  @JsonIgnore
  private String password;
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
  @JsonIgnore // when we transorm java object into json string, will not put authorities inside user object
  private List<Authority> authorities = new ArrayList<>();
  private String name;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public LocalDate getCohortStartDate() {
    return cohortStartDate;
  }
  public void setCohortStartDate(LocalDate cohortStartDate) {
    this.cohortStartDate = cohortStartDate;
  }
  @Override // need to override because UserDetails has this method so we want to use our own custom method
  public String getUsername() {
    return this.username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  @Override  // need to override because UserDetails has this method so we want to use our own custom method
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() { 
    // authorities is a ROLE
    // needs to return a role
    // this code will generate a list of roles that is calling the Authority class with the role that is specified
    // List<GrantedAuthority> roles = new ArrayList<>();
    // roles.add(new Authority("ROLE_STUDENT"));
    // return roles;
    return authorities;
  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
  @Override
  public boolean isEnabled() {
    return true;
  }

}
