package com.example.backend.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

import com.example.backend.domain.Assignment;
import com.example.backend.domain.Comment;
import com.example.backend.domain.User;
import com.example.backend.dto.CommentDto;
import com.example.backend.repository.AssignmentRepository;
import com.example.backend.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

  @Autowired
  private CommentRepository commentRepo;

  @Autowired
  private AssignmentRepository assignmentRepo;

  public Comment save(CommentDto commentDto, User user) {
    Comment comment = new Comment();
    Assignment assignment = assignmentRepo.getById(commentDto.getAssignmentId());
    comment.setAssignment(assignment);
    comment.setText(commentDto.getText());
    comment.setCreatedBy(user);
    comment.setId(commentDto.getId());
    if(comment.getId() == null)
      comment.setCreatedDate(ZonedDateTime.now());
    else 
      comment.setCreatedDate(commentDto.getCreatedDate());
    
    return commentRepo.save(comment);
  }

  public Set<Comment> getCommentsByAssignmentId(Long assignmentId) {
    Set<Comment> comments = commentRepo.findByAssignmentId(assignmentId);
    return comments;
  }

  public void delete(Long commentId) {
    commentRepo.deleteById(commentId);
    
}
  
} 
