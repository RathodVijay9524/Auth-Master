package com.vijay.auth.service;

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
import com.vijay.auth.exception.BlogAPIException;
import com.vijay.auth.model.UserDto;
import com.vijay.auth.repository.RoleRepo;
import com.vijay.auth.repository.UserRepository;
import com.vijay.auth.repository.WorkerRepository;
import com.vijay.auth.request.LoginRequest;
import com.vijay.auth.request.RegistraonRequest;
import com.vijay.auth.response.LoginJWTResponse;
import com.vijay.auth.response.RegistraonResponse;
import com.vijay.auth.security.JwtTokenProvider;

import java.util.HashSet;

import java.util.Set;

@Service
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
		if (userRepository.existsByUsername(req.getUsername())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
		}

		// add check for email exists in database
		if (userRepository.existsByEmail(req.getEmail())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
		}

		//User user = new User();
	    User user = mapper.map(req, User.class);
		user.setPassword(passwordEncoder.encode(req.getPassword()));
		Role userRole = roleRepository.findByName("ROLE_NORMAL").get();
		user.getRoles().add(userRole);
		userRepository.save(user);

		return "User registered successfully!.";
	}

	@Override
	public RegistraonResponse registerWorker(RegistraonRequest req) {
		// add check for username exists in database
		if (userRepository.existsByUsername(req.getUsername())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
		}

		// add check for email exists in database
		if (userRepository.existsByEmail(req.getEmail())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
		}

		UserDto currentUser = userService.getCurrentUser();
		User user = mapper.map(currentUser, User.class);

		Worker worker = mapper.map(req, Worker.class);
		worker.setPassword(passwordEncoder.encode(req.getPassword()));
		Role userRole = roleRepository.findByName("ROLE_WORKER").get();
		worker.getRoles().add(userRole);
		worker.setUser(user);
		workerRepository.save(worker);

		return mapper.map(worker, RegistraonResponse.class);
	}

}
