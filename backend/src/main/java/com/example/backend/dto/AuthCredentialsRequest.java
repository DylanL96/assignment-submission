package com.example.backend.dto;

// this is a DTO
// will not be stored in DB, just used to pass data along
public class AuthCredentialsRequest {
  private String username;
  private String password;
  
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  
}
