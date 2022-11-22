package com.photoApp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.photoApp.api.users.shared.UserDto;

public interface UserService  extends UserDetailsService{
	public UserDto createUser(UserDto userDto);
	public UserDto getUserDetailsByEmail(String email);
	public UserDto getUserByUserId(String userId);
}
