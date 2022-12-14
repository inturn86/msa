package com.sdc.userservice.vo;

import javax.inject.Singleton;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RequestLogin {

	@NotNull(message = "Email cannot be null")
	@Size(min = 2, message = "Email not be less than two chars")
	@Email
	private String email;

	@NotNull(message = "Password cannot be null")
	@Size(min = 8, message = "Password must be equals or greater than 8 chars")
	private String password;
}
