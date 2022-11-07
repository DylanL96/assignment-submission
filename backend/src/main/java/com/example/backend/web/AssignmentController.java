package com.example.backend.web;

import java.util.Optional;
import java.util.Set;

import com.example.backend.Service.AssignmentService;
import com.example.backend.Service.UserService;
import com.example.backend.domain.Assignment;
import com.example.backend.domain.User;
import com.example.backend.dto.AssignmentResponseDto;
import com.example.backend.enums.AuthorityEnum;
import com.example.backend.util.AuthorityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

  // Controllers only job is just calling the service, getting the data from the service and sending back to front end
  @Autowired
  private AssignmentService assignmentService;

  @Autowired
  private UserService userService;
  
  @PostMapping("") // post to itself
  public ResponseEntity<?> createAssignment(@AuthenticationPrincipal User user){
    // Instantiate an Assignment object called newAssignment
    // This will invoke the assignmentService method SAVE which saves an AUTHENTICATED user!
    Assignment newAssignment = assignmentService.save(user);

    return ResponseEntity.ok(newAssignment);
  }

  @GetMapping("") // this method to get all of the assignments data
  public ResponseEntity<?> getAssignments(@AuthenticationPrincipal User user){
    // this will give us a list of assignments by student role
    Set<Assignment> assignmentsByUser = assignmentService.findByUser(user); 
    return ResponseEntity.ok(assignmentsByUser); 
  }

  // this endpoint is for the AssignmentView for a specific assignment ID
  // assignmentId has to match {assignmentId}
  // the value of {assignmentId} will be injected into the PathVariable and we will have access to it
  @GetMapping("{assignmentId}") 
  public ResponseEntity<?> getAssignment(@PathVariable Long assignmentId, @AuthenticationPrincipal User user){
    Optional<Assignment> assignmentOpt = assignmentService.findById(assignmentId);

    // this will return the AssignmentEnum to populate the dropdown menu
    AssignmentResponseDto response = new AssignmentResponseDto(assignmentOpt.orElse(new Assignment()));
    return ResponseEntity.ok(response);
  }

  @PutMapping("{assignmentId}") 
  public ResponseEntity<?> updateAssignment(@PathVariable Long assignmentId, @RequestBody Assignment assignment, @AuthenticationPrincipal User user){
    // communicates with CodeReviewerDashboard
    // add code reviewer to this assignment if it was claimed
    if(assignment.getCodeReviewer() != null){
      User codeReviewer = assignment.getCodeReviewer();
      codeReviewer = userService.findUserByUsername(codeReviewer.getUsername()).orElse(new User());

      if(AuthorityUtil.hasRole(AuthorityEnum.ROLE_CODE_REVIEWER.name(), codeReviewer)){
        assignment.setCodeReviewer(codeReviewer);
      }
    }
    // this code just saves the assignment object to the repo
   Assignment updatedAssignment = assignmentService.save(assignment);
   return ResponseEntity.ok(updatedAssignment);  
  }
}
