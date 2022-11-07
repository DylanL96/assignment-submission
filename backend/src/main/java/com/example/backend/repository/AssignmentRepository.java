package com.example.backend.repository;

import java.util.Set;

import com.example.backend.domain.Assignment;
import com.example.backend.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // findBy is a reserved phrase and will create a custom query with the findBy in combination with the User
    // We want to return a set list of the assignments that is saved in the DB
    Set<Assignment>findByUser(User user);

    // findBy implies a property inside the object we are querying on the right side of findBy exists, but there is no "CodeReviewer" property
    // we can create a custom query using @Query

    // give all assignments where status is "submitted"
    // a.codeReviewer is coming from Assignment which codeReviewer has type User
    @Query("select a from Assignment a " 
    + "where (a.status = 'submitted' and (a.codeReviewer is null or a.codeReviewer = :codeReviewer)) " 
    + "or a.codeReviewer = :codeReviewer") 
    Set<Assignment> findByCodeReviewer(User codeReviewer);

    // Set<Comment> findByAssignmentId(Long assignmentId);
}
