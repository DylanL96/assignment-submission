package com.example.backend.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component // going to annotate it so it is managed by spring and can be injected elsewhere
public class JwtFilter extends OncePerRequestFilter { //only want this filter to happen once per request
  // JwtFilter is tied to spring security and part of the SS chain and will go through the steps of authenticating and authorizing the user
  @Autowired
  private UserRepository userRepo;
  // json web token is a string that contains info to validate a user is who they say they are
  // token goes through a process of being signed. There is expiry date of this token.
  // token has basic info about the user. So when user logs in, when the user sends a request,
  // we can use the token to show who the token is and then grant authentication and authorization
  // can pass JWT with every request, which makes it stateless

  // we have utility object that will handle the token and claims to use the jwt
  @Autowired JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    
        // Get authorization header and validate
        // when user submits request from front end
        // SS will intercept the request and check the request
        // if this is NOT a JWT, we will discard it
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(header) || StringUtils.hasText(header) && !header.startsWith("Bearer ")){
          chain.doFilter(request, response); //chain.DoFilter will pass the JWT to the next filter in the chain of filters
          return;
        }

        // if we get passed the first method above, it means we have a bearer token
        // this line of code will split it
        // our request looks like: Authorization(KEY) -> (VALUE)Bearer 
        // writing code to take the Bearer value and split it by a space
        // example: ([Bearer], [fjOIEWF(*#$JJLFWEKLFJELWF90293)])
        // we ONLY want the token which is the nasty password thing and then assign it to the token variable
        final String token = header.split(" ")[1].trim();

        // we have set the code to the token variable
        // Get user identity and set it on the spring security context
        // will use the token and look up the username on the User repository
        UserDetails userDetails = userRepo
                .findByUsername(jwtUtil.getUsernameFromToken(token)) // grab the username from the token
                .orElse(null);

        // get jwt token and validate
        // grabs username from token and ensures it matches the username in the UserDetails object
        // then checks if its expired
        // this will return true
        // if it does NOT return true, it will NOT validate and will send to next chain in the filter chain
        if(!jwtUtil.validateToken(token, userDetails)){
          chain.doFilter(request, response);
        }

        // principal is User Details
        UsernamePasswordAuthenticationToken
          authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails == null ? List.of() : userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // where authentication happens and user is now valid
        // SS method of validating the user and injecting them into our spring context
        // this will result in them having access to their level of authority
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    
  }
}
