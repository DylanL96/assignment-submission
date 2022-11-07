package com.example.backend.web;

import com.example.backend.domain.User;
import com.example.backend.dto.AuthCredentialsRequest;
import com.example.backend.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  // *When dealing with REST endpoints, you want to return ResponseEntity<?>
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("login") // Http://localhost:8080/api/auth/login
  public ResponseEntity<?> login (@RequestBody AuthCredentialsRequest request){ // pass in our request
    // this method will receieve credentials, and then validate the credentials
    // RequestBody is important because when we send the POST request, it will bind the information between the request and the server side data
    try{
      Authentication authenticate = authenticationManager
          .authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
          );
      User user = (User) authenticate.getPrincipal();
      user.setPassword(null); // this will then remove the password from being exposed

      return ResponseEntity.ok() 
          .header(
              HttpHeaders.AUTHORIZATION,
              jwtUtil.generateToken(user) // generates our JWT
          )
          .body((user)); // return details of user in the body without all of the sensitive info such as password
    } catch (BadCredentialsException ex){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
  // use @RequestParam because since its GET request, we pass data with @RequestParam when dealing with GET requests
  // for example: localhost:8080/api/auth/validate?token=blahblahblah
  // the ?token=blahblahblah is the @RequestParam that joins those info to the URL
  @GetMapping("/validate") 
  public ResponseEntity<?> validateToken(@RequestParam String token, @AuthenticationPrincipal User user){
    // validateToken takes in token and UserDetails
    // we can pass User user because it implicitly passes in UserDetails
    // this is because User implements UserDetails
    try{
      Boolean isTokenValid = jwtUtil.validateToken(token, user);
      return ResponseEntity.ok(isTokenValid);
    } catch (ExpiredJwtException e){
        return ResponseEntity.ok(false);
    }
    
  }
  
}
