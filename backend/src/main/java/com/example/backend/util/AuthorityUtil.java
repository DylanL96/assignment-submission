package com.example.backend.util;

import com.example.backend.domain.User;

public class AuthorityUtil {
  public static Boolean hasRole(String role, User user){
    // This class will stream, filter and find that role
    // if it has, return true
    // else, return false
    return user.getAuthorities()
          .stream()
          .filter(auth -> auth.getAuthority().equals(role))
          .count() > 0;
  }
}
