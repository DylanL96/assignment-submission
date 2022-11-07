package com.example.backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder {
  // this component will return BCryptPasswordEncoder

  private PasswordEncoder passwordEncoder;
  
  public CustomPasswordEncoder(){
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  public PasswordEncoder getPasswordEncoder() {
    return passwordEncoder;
  }


  
}
