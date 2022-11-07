package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}

// Spring Data JPA is a layer of abstraction, removing a lot of hibernate boiler plate code which allows us to write less code while doing all of the CRUD functions. Normally have to code all of those SQL queries, but Spring Data JPA allows us to not have to do that.

// Using JWT is stateless which means there is no state stored for specific authentication request coming from front end. This is important when we are dealing with full-stack applications

// Spring Security has filters which are layers of security which are required to be passed in order to gain authorization and authentication into the server.
// Create custom JWT filter to validate the JWT 