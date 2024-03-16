package com.vijay.auth.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.auth.model.UserDto;
import com.vijay.auth.request.LoginRequest;
import com.vijay.auth.request.RegistraonRequest;
import com.vijay.auth.response.LoginJWTResponse;
import com.vijay.auth.response.RegistraonResponse;
import com.vijay.auth.service.AuthService;
import com.vijay.auth.service.UserService;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

	 @Autowired
	 private AuthService authService;
	 
	 @Autowired
	 private UserDetailsService userDetailsService;
	 
	 @Autowired
	 private UserService userService;
	 
	    @Autowired
	    private ModelMapper modelMapper;
	 
		
	  
	    // Build Login REST API
	    @PostMapping(value = {"/login", "/signin"})
	    public ResponseEntity<LoginJWTResponse> login(@RequestBody LoginRequest request){
	         LoginJWTResponse login = authService.login(request);
	        return ResponseEntity.ok(login);
	    }

	    // Build Register REST API
	    @PostMapping(value = {"/signup"})
	    public ResponseEntity<String> register(@RequestBody RegistraonRequest request){
	        String response = authService.register(request);
	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    }
	    @PostMapping("/worker")
	    public ResponseEntity<RegistraonResponse> registerWorker(@RequestBody RegistraonRequest registerDto){
	        RegistraonResponse registerWorker = authService.registerWorker(registerDto);
	        return new ResponseEntity<>(registerWorker, HttpStatus.CREATED);
	    }
	    
	    @GetMapping("/currunt")
	    public ResponseEntity<UserDto> curruntUser() {
	    	 UserDto currentUser = userService.getCurrentUser();
	    	return new ResponseEntity<>(currentUser, HttpStatus.OK);
	    }
	    
	    @GetMapping("/currunts")
	    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
	        return new ResponseEntity<>(modelMapper
	                .map(userDetailsService.loadUserByUsername(principal.getName()),
	                		UserDto.class), HttpStatus.OK);
	    }
	    
	 
	    
	    
}
