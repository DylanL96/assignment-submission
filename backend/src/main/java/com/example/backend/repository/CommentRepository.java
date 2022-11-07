package com.example.backend.repository;

import java.util.Set;

import com.example.backend.domain.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  // get me all the comments that have this assignmentId
  // then assign it to a set
  @Query("select c from Comment c " 
          + " where c.assignment.id = :assignmentId")
  Set<Comment> findByAssignmentId(Long assignmentId);
  
}
