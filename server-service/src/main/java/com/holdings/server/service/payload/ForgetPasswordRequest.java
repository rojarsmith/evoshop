package com.holdings.server.service.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ForgetPasswordRequest {
	@NotBlank
	@Email
	private String email;
}
