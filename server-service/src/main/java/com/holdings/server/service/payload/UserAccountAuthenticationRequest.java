package com.holdings.server.service.payload;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserAccountAuthenticationRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
