package com.vijay.auth.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vijay.auth.entity.User;
import com.vijay.auth.exception.BlogAPIException;
import com.vijay.auth.exception.ResourceNotFoundException;
import com.vijay.auth.helper.Helper;
import com.vijay.auth.model.UserDto;
import com.vijay.auth.repository.UserRepository;
import com.vijay.auth.response.PageableResponse;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDto getCurrentUser() {
		// Retrieve the current authentication object
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Check if authentication is null or if the user is anonymous
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken) {
			throw new IllegalStateException("User is not authenticated.");
		}
		String username = authentication.getName();
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		// Check if userDetails is null (user not found)
		if (userDetails == null) {
			throw new IllegalStateException("User details not found.");
		}
		return mapper.map(userDetails, UserDto.class);
	}

	@Override
	public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		// pageNumber default starts from 0
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<User> page = userRepository.findAll(pageable);

		PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);

		return response;
	}

	@Override
	public UserDto findUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(Long userId, UserDto userDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		user.setName(userDto.getName());
		user.setName(userDto.getName());
		user.setUsername(userDto.getUsername());
		// user.setEmail(userDto.getEmail());
		if (!userDto.getPassword().equalsIgnoreCase(user.getPassword()))
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		// save data
		User updatedUser = userRepository.save(user);
		return mapper.map(updatedUser, UserDto.class);
	}

	@Override
	public void deleteUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		userRepository.delete(user);

	}

	@Override
	public UserDto getUserByEmail(String email) {
		Optional<User> findByEmail = userRepository.findByEmail(email);

		try {
			User user = findByEmail.orElseThrow();
			return mapper.map(user, UserDto.class);
		} catch (NoSuchElementException ex) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "User Email Is NotFound");
		}
	}

	@Override
	public List<UserDto> searchUser(String keywords) {
		List<User> users = userRepository.findByNameContaining(keywords);
		return users.stream()
				.map(user -> mapper.map(user, UserDto.class))
				.collect(Collectors.toList());
	}

}
