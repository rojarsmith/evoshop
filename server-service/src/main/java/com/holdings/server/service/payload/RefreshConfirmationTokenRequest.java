package com.holdings.server.service.payload;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RefreshConfirmationTokenRequest {
	@NotBlank
	private String userName;
}
