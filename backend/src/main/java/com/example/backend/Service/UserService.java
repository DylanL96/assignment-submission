package com.example.backend.Service;

import java.util.Optional;

import com.example.backend.domain.User;
import com.example.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepo;
  
  public Optional<User> findUserByUsername(String username){
    return userRepo.findByUsername(username);
  }
}
