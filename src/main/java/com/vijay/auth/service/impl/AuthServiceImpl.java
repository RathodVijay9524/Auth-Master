package com.vijay.auth.service.impl;

import com.vijay.auth.service.AuthService;
import com.vijay.auth.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vijay.auth.entity.Role;
import com.vijay.auth.entity.User;
import com.vijay.auth.entity.Worker;
import com.vijay.auth.exception.model.BlogAPIException;
import com.vijay.auth.entity.model.UserDto;
import com.vijay.auth.repository.RoleRepo;
import com.vijay.auth.repository.UserRepository;
import com.vijay.auth.repository.WorkerRepository;
import com.vijay.auth.entity.request.LoginRequest;
import com.vijay.auth.entity.request.RegistraonRequest;
import com.vijay.auth.entity.response.LoginJWTResponse;
import com.vijay.auth.entity.response.RegistraonResponse;
import com.vijay.auth.config.security.JwtTokenProvider;

import java.util.HashSet;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private UserService userService;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private WorkerRepository workerRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepo roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public LoginJWTResponse login(LoginRequest req) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(req.getUsernameOrEmail(), req.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsernameOrEmail());

		String token = jwtTokenProvider.generateToken(authentication);

		UserDto response = mapper.map(userDetails, UserDto.class);

		LoginJWTResponse jwtResponse = LoginJWTResponse.builder()
				.jwtToken(token)
				.user(response).build();

		return jwtResponse;
	}

	@Override
	public String register(RegistraonRequest req) {

		// add check for username exists in database
		if (userService.existsByUsernameOrEmail(req.getUsername()) || userService.existsByUsernameOrEmail(req.getEmail())) {
			log.error("Username '{}' or email '{}' already exists", req.getUsername(), req.getEmail());
			throw new RuntimeException("Username or email is already taken");
		}

		// Map the request to a new User object
		User user = mapper.map(req, User.class);

		// Set the password after encoding
		user.setPassword(passwordEncoder.encode(req.getPassword()));

		// Fetch the user role from the repository
		Role userRole = roleRepository.findByName("ROLE_NORMAL").orElseThrow(() -> new BlogAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found."));

		// Initialize the roles field if it's null
		if (user.getRoles() == null) {
			user.setRoles(new HashSet<>());
		}

		// Add the user role to the roles set
		user.getRoles().add(userRole);

		// Save the user to the database
		userRepository.save(user);

		return "User registered successfully.";
	}

	@Override
	public RegistraonResponse registerWorker(RegistraonRequest req) {
		// add check for username exists in database
		if (userService.existsByUsernameOrEmail(req.getUsername()) || userService.existsByUsernameOrEmail(req.getEmail())) {
			log.error("Username '{}' or email '{}' already exists", req.getUsername(), req.getEmail());
			throw new RuntimeException("Username or email is already taken");
		}

		UserDto currentUser = userService.getCurrentUser();
		User user = mapper.map(currentUser, User.class);

		Worker worker = mapper.map(req, Worker.class);
		worker.setPassword(passwordEncoder.encode(req.getPassword()));

		// Fetch the worker role from the repository
		Role workerRole = roleRepository.findByName("ROLE_WORKER")
				.orElseThrow(() -> new BlogAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Worker role not found."));

		// Initialize the roles field if it's null
		if (worker.getRoles() == null) {
			worker.setRoles(new HashSet<>());
		}

		// Add the worker role to the roles set
		worker.getRoles().add(workerRole);

		worker.setUser(user);
		workerRepository.save(worker);

		return mapper.map(worker, RegistraonResponse.class);
	}

}
