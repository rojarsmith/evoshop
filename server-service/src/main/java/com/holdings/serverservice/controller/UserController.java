package com.holdings.serverservice.controller;

import java.net.URI;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.holdings.serverservice.ServiceConfig;
import com.holdings.serverservice.entity.ConfirmationToken;
import com.holdings.serverservice.entity.UserAccount;
import com.holdings.serverservice.payload.UserAccountCreationRequest;
import com.holdings.serverservice.repository.ConfirmationTokenRepository;
import com.holdings.serverservice.repository.UserAccountRepository;
import com.holdings.serverservice.service.MailContentBuilderService;
import com.holdings.serverservice.service.MailContentBuilderService.Template;
import com.holdings.serverservice.utility.IpUtils;
import com.holdings.serverservice.utility.Miscellaneous;
import com.holdings.serverservice.utility.ValueValidate;

@RestController
@RequestMapping(value = { "/api" })
public class UserController {
	@Autowired
	private ServiceConfig serviceConfig;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailContentBuilderService mailContentBuilderService;

	@Autowired
	private Miscellaneous miscellaneous;

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

		// Creating user's account
		UserAccount user = new UserAccount();

		user.setUserName(userAccountCreationRequest.getUserName());
		user.setPassword(passwordEncoder.encode(userAccountCreationRequest.getPassword()));
		user.setEmail(userAccountCreationRequest.getEmail());
		user.setEmailVerified(false);
		user.setRegisterTime(Instant.now());
		user.setRegisterIp(IpUtils.getIpAddr(request));
		user.setActived(true);
		UserAccount newUser = userAccountRepository.save(user);

		String path = miscellaneous.getRequestMappingPath("userSignup(");

		// Confirmation Mail
		ConfirmationToken token = new ConfirmationToken(user);
		while (confirmationTokenRepository.existsByConfirmationToken(token.getConfirmationToken())) {
			token.freshToken();
		}
		confirmationTokenRepository.save(token);

		URI locationConfirm = ServletUriComponentsBuilder
				.fromUriString(serviceConfig.getServiceDomain() + "/confirm-account/" + token.getConfirmationToken())
				.buildAndExpand().encode().toUri();

		String[] mailList = { newUser.getEmail() };
		String mailContent = mailContentBuilderService.generateMailContent(locationConfirm.toString(),
				Template.SIGN_UP);

		return ResponseEntity.created(null).body(null);
	}
}
