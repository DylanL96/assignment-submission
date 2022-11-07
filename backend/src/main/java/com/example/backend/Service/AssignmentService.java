package com.example.backend.Service;

import java.util.Optional;
import java.util.Set;

import com.example.backend.domain.Assignment;
import com.example.backend.domain.User;
import com.example.backend.enums.AssignmentStatusEnum;
import com.example.backend.enums.AuthorityEnum;
import com.example.backend.repository.AssignmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

  @Autowired
  private AssignmentRepository assignmentRepo;

  // This is a save method that was created in the Service class
  // creating brand new assignment
  public Assignment save(User user){
    Assignment assignment = new Assignment();
    assignment.setStatus(AssignmentStatusEnum.PENDING_SUBMISSION.getStatus());
    assignment.setNumber(findNextAssignmentToSubmit(user));
    assignment.setUser(user);

    // saves the assignment into the assignmentRepo
    // JPA will communicate with SQL and save the data into the database
    return assignmentRepo.save(assignment); // this will return an Assignment
  }

  private Integer findNextAssignmentToSubmit(User user) {
    // give set of assignments that the user has submitted
    // with this set of assignments, we can determine
    // which assignment should be submitted next
    Set<Assignment> assignmentsByUser = assignmentRepo.findByUser(user);
    if(assignmentsByUser == null){
      return 1;
    }
    // can pass a comparator .sorted() 
    // comparator takes 2 objects that are being compared
    // i.e. compare assignment object #1 and assignment object#2
    // and then we can compare them and sort based on assignment number
    // this algorithm will sort in descending order with highest assignment number first
    // and will detect the next assignment the user should theoretically submit
    // i.e. if they submitted assignment 1, theoretically they would submit assignment 2 next

    Optional<Integer> nextAssignmentNumOpt = assignmentsByUser.stream() 
                      .sorted((a1, a2)->{
                          if(a1.getNumber() == null) return 1;
                          if(a2.getNumber() == null) return 1;
                          return a2.getNumber().compareTo(a1.getNumber());
                      })
                      .map(assignment -> {
                        if(assignment.getNumber() == null) 
                          return 1;
                        return assignment.getNumber() + 1;
                      })
                      .findFirst();
    //  if dont find anything, return 1
    return nextAssignmentNumOpt.orElse(1);

  }

  public Set<Assignment> findByUser(User user){
    // checks the role of the user
    Boolean hasCodeReviewerRole = user.getAuthorities()
        .stream()
        .filter(auth -> AuthorityEnum.ROLE_CODE_REVIEWER.name().equals(auth.getAuthority()))
        .count() > 0; // if >0, then we have a code reviewer role

    if(hasCodeReviewerRole){
      // load assignments if you're code reviewer role
      return assignmentRepo.findByCodeReviewer(user);
    } else {
      // load assignments if you're student role
      return assignmentRepo.findByUser(user);
    }
  }

  public Optional<Assignment> findById(Long assignmentId) {
    return assignmentRepo.findById(assignmentId);
  }

  // updating assignment
  public Assignment save(Assignment assignment) {
    return assignmentRepo.save(assignment);
  }
}
