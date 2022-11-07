package com.example.backend.Service;

import java.util.Optional;

import com.example.backend.domain.User;
import com.example.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// UserDetailsService comes from Spring Security
// UserDetailsService is an interface so we have to override this method if we want to fulfill this contract
@Service // indicates managed by Spring. This means this class can be injected because its managed by Spring
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // The idea of this class is to query from our DB, we can load a user from DB via their username and see if their passwords match!
    // This method will talk to our repository to see if a user exists within that repository
    // findBy is a special prefix and Spring is smart enough to understand what we want to do 
    Optional<User> userOpt = userRepo.findByUsername(username); //findByUsername is a custom method we are creating in the UserRepository
    return userOpt.orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));

    // Hard coding the User for testing purposes
    // User user = new User();
    // user.setUsername(username);
    // // Think this is the encoded password, so we have to use the injected passwordEncoder to encode the password for us
    // user.setPassword(passwordEncoder.getPasswordEncoder().encode("password")); // encodes "password" as a string and stores it properly so it is encoded and then gets decoded once the user enters the password to log in
    // user.setId(1L);
    // return user;
  }
  
}
