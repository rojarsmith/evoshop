package com.holdings.serverservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.holdings.serverservice.payload.UserAccountCreationRequest;
import com.holdings.serverservice.repository.UserAccountRepository;

@RestController
@RequestMapping(value = { "/api" })
public class UserController {
	@Autowired
	private UserAccountRepository userAccountRepository;

	@PostMapping(value = { "/v{version:\\d}/user/signup" })
	public ResponseEntity<?> userSignup(HttpServletRequest request,
			@Valid 
			@RequestBody UserAccountCreationRequest userAccountCreationRequest) throws Exception {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name validate failed.");
//		return ResponseEntity.ok(null);
	}
}
