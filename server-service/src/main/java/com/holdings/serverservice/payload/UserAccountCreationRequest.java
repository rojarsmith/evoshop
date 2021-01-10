package com.holdings.serverservice.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserAccountCreationRequest {
	@NotBlank
	private String userName;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;
}
