package com.vijay.auth.service;

import java.util.List;

import com.vijay.auth.entity.model.UserDto;
import com.vijay.auth.entity.response.PageableResponse;

public interface UserService {

	boolean existsByUsernameOrEmail(String usernameOrEmail);
	UserDto getCurrentUser();

	PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

	UserDto findUserById(Long userId);

	UserDto updateUser(Long userId, UserDto userDto);

	void deleteUserById(Long userId);

	UserDto getUserByEmail(String email);

	List<UserDto> searchUser(String keywords);

}
