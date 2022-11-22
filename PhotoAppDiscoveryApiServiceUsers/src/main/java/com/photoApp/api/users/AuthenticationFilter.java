package com.photoApp.api.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photoApp.api.users.service.UserService;
import com.photoApp.api.users.shared.UserDto;
import com.photoApp.api.users.ui.model.LoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter  extends UsernamePasswordAuthenticationFilter{
	
	UserService userService;
	Environment environment;
	@Autowired
	public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment environment) {
		super.setAuthenticationManager(authenticationManager);
		this.userService  = userService;
		this.environment = environment;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException{
		
		try {
			LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(),LoginRequestModel.class);
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(),
							creds.getPassword(),
							new ArrayList<>())
				);
					
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											 FilterChain chain,
											 Authentication auth) throws IOException, ServletException{
		String username = ((User) auth.getPrincipal()).getUsername();
		UserDto userDetails = userService.getUserDetailsByEmail(username);
		
		String token = Jwts.builder()
				.setSubject(userDetails.getUserId())
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration.io"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.compact();
		
		res.addHeader("token", token);
		res.addHeader("value", userDetails.getUserId());
		
	}
}
