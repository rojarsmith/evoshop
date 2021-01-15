package com.holdings.server.service.payload;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	@NotBlank
	private String password;

	@NotBlank
	private String token;
}
