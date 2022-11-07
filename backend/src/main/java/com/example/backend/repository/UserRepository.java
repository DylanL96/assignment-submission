package com.example.backend.repository;

import java.util.Optional;

import com.example.backend.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

// By extending JpaRepository, it allows us to have all of the CRUD functionalities
// without having to write SQL queries
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username); // Spring will write the specific SQL query for us to perform the method!
  
}
