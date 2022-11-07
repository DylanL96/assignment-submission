package com.example.backend.web;

import java.util.Set;

import com.example.backend.Service.CommentService;
import com.example.backend.domain.Comment;
import com.example.backend.domain.User;
import com.example.backend.dto.CommentDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Rest controllers return data not views
// if these endpoints were returning views, it would be @Controller
// RestController implies returning data
@RestController
@RequestMapping("/api/comments") // make the prefix with this
public class CommentController {

  // passing in actual comment itself, and since it is a POST, we use @RequestBody annotation
  // this is because @RequestBody will have the data in it since it is a POST request
  // remember that controllers talk to services and not to the DB directly.
  @Autowired
  private CommentService commentService;

  @PostMapping("")
  public ResponseEntity<Comment> createComment(@RequestBody CommentDto commentDto, @AuthenticationPrincipal User user){
    // create CommentDto to change how the data is being sent
    // @AuthenticationPrincipal is important because it allows us to get the User object
    Comment comment = commentService.save(commentDto, user);
    return ResponseEntity.ok(comment);
  }

  // @RequestParam is part of the URL it self
  @GetMapping("")
  public ResponseEntity<Set<Comment>> getCommentsByAssignment(@RequestParam Long assignmentId){
    Set<Comment> comments = commentService.getCommentsByAssignmentId(assignmentId);
    return ResponseEntity.ok(comments);
  }

  @PutMapping("{commentId}")
  public ResponseEntity<Comment> updateComment(@RequestBody CommentDto commentDto, @AuthenticationPrincipal User user){
    Comment comment = commentService.save(commentDto, user);
    return ResponseEntity.ok(comment);
  }
  @DeleteMapping("{commentId}")
  public ResponseEntity<?> deleteComment (@PathVariable Long commentId) {
      try {
          commentService.delete(commentId);
          return ResponseEntity.ok("Comment deleted");
      } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
      
  }
  
}
