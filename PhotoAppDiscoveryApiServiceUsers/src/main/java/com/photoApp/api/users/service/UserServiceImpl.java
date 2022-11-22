package com.photoApp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.photoApp.api.users.data.UserRepository;
import com.photoApp.api.users.data.UsersEntity;
import com.photoApp.api.users.shared.UserDto;
import com.photoApp.api.users.ui.model.AlbumResponseModel;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	CallBackMethod callBackMethod;
	
	@Bean(name="pass")
	public BCryptPasswordEncoder passwordEncoder()
	{
	    return new BCryptPasswordEncoder();
	}
	
	@Override
	public UserDto createUser(UserDto userDto) {

		userDto.setUserId(UUID.randomUUID().toString());
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UsersEntity userEntity =modelMapper.map(userDto, UsersEntity.class);
		userEntity.setEncryptedPassword(passwordEncoder().encode(userDto.getPassword()));
		UserDto ud = modelMapper.map(userRepository.save(userEntity), UserDto.class);
		return ud;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UsersEntity userEntity = userRepository.findByEmail(username);
		
		if(userEntity == null) throw new UsernameNotFoundException(username);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email)throws UsernameNotFoundException {
		UsersEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		return new ModelMapper().map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		UsersEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) throw new UsernameNotFoundException("user does not exists");
		
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
//		String url =String.format("http://ALBUMS-WS/users/%s/albums", userId);
//		ResponseEntity<List<AlbumResponseModel>> res = restTemplate.exchange(url, HttpMethod.GET,null,new ParameterizedTypeReference<List<AlbumResponseModel>>() {});
		
		List<AlbumResponseModel> alb = callBackMethod.getAlbums(userId);
//		List<AlbumResponseModel> alb = res.getBody();
		
		userDto.setAlbums(alb);
		
		return userDto;
	}

}
