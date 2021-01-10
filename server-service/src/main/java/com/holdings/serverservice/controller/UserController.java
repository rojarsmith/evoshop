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
import com.holdings.serverservice.utility.ValueValidate;

@RestController
@RequestMapping(value = { "/api" })
public class UserController {
	@Autowired
	private UserAccountRepository userAccountRepository;

	@PostMapping(value = { "/v{version:\\d}/user/signup" })
	public ResponseEntity<?> userSignup(HttpServletRequest request,
			@Valid @RequestBody UserAccountCreationRequest userAccountCreationRequest) throws Exception {
		if (!ValueValidate.validateUserName(userAccountCreationRequest.getUserName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name validate failed.");
		}
		
        if (!ValueValidate.validateEmail(userAccountCreationRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email validate failed.");
        }

        if (!ValueValidate.validatePassword(userAccountCreationRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password validate failed.");
        }
        
        if (userAccountRepository.existsByUserName(userAccountCreationRequest.getUserName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name already in use.");
        }

        if (userAccountRepository.existsByEmail(userAccountCreationRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address already in use.");
        }

		return ResponseEntity.created(null).body(null);
	}
}
