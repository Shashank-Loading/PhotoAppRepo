package com.photoApp.api.users.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequestModel {
	
	@NotNull(message="firstName cannot be null")
	@Size(min=2,message="firstName must not be less than two digits")
	private String firstName;
	
	@NotNull(message="lastName cannot be null")
	@Size(min=2,message="lastName must not be less than two digits")
	private String lastName;
	
	@NotNull(message="password cannot be null")
	@Size(min=8, max=16, message="password must be in between 8 and 16 characters")
	private String password;
	
	@Email(message = "email format is incorrect")
	private String email;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
