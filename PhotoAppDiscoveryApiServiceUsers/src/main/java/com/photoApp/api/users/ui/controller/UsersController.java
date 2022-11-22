package com.photoApp.api.users.ui.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photoApp.api.users.service.UserService;
import com.photoApp.api.users.shared.UserDto;
import com.photoApp.api.users.ui.model.CreateUserResponseModel;
import com.photoApp.api.users.ui.model.UserRequestModel;
import com.photoApp.api.users.ui.model.UserResponseModel;

@RestController
@RequestMapping(path="/users")
public class UsersController {
	
	@Autowired
	private Environment env;
	
	@Autowired
	public UserService userService;
	
	
	@GetMapping("/status/check")
	public String getString() {
		return "Working on port: "+env.getProperty("local.server.port");
	}
	
	@PostMapping
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody UserRequestModel userDetails) {
		
		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto userDto1=userService.createUser(userDto);
		CreateUserResponseModel response = modelMapper.map(userDto1, CreateUserResponseModel.class);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	@GetMapping("/hi")
	public String hi() {
		return "hi";
	}
	@GetMapping(value="{userId}")
	public ResponseEntity<UserResponseModel> albums(@PathVariable("userId") String userId) {
		
		UserDto userDto = userService.getUserByUserId(userId);
		UserResponseModel mod = new ModelMapper().map(userDto,UserResponseModel.class);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(mod);
	}

}
