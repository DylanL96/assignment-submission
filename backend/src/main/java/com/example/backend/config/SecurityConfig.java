package com.example.backend.config;

import javax.servlet.http.HttpServletResponse;

import com.example.backend.filter.JwtFilter;
import com.example.backend.util.CustomPasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  // Already some default behaviour that is from Spring Security(SS), such as the login page 
  // The default behaviour is coming from these two methods below which should be overriden
  // This default functionality is NOT desirable so  we want to override it
  // I.E. we want a stateless security mechanism, which is the JWT 
  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private CustomPasswordEncoder customPasswordEncoder;

  @Autowired
  private JwtFilter jwtFilter;

  @Override @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception{
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // userDetailsService is a service used to load users from our database
    // userDetailsService will come from UserDetailsServiceImpl.java class
    // In security config, along with userDetailsService, we need a password encoder
    // Typically we use BCryptPasswordEncoder to do this for us
    // getPasswordEncoder() returns a passwordEncoder, which returns the passwordEncoder
    auth.userDetailsService(userDetailsService) // this code will validate our user and do authentication
        .passwordEncoder(customPasswordEncoder.getPasswordEncoder()); 
        //method where we are supplying userDetailsService to auth
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http = http.csrf().disable().cors().disable(); //CORS is a method to prevent hacking

    http = http.sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and();

    http = http.exceptionHandling()
               .authenticationEntryPoint((request, response, ex) -> {
                  response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
               }).and();

    http.authorizeRequests()  // forcefully make all requests authenticated  
        .antMatchers("/api/auth/**").permitAll() // permits all traffic to come to this specific endpoint. wil not do any security filtering
        .anyRequest().authenticated();

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  }

  // @Bean
  // public PasswordEncoder passwordEncoder(){ 
  //   // In security config, along with userDetailsService, we need a password encoder
  //   // Typically we use BCryptPasswordEncoder to do this for us
  //   return new BCryptPasswordEncoder();
  // } 
}
