package com.holdings.server.service.controller;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.holdings.server.service.ServiceConfig;
import com.holdings.server.service.entity.ConfirmationToken;
import com.holdings.server.service.entity.UserAccount;
import com.holdings.server.service.extra.EmailSenderService;
import com.holdings.server.service.extra.MailContentBuilder;
import com.holdings.server.service.extra.MailContentBuilder.Template;
import com.holdings.server.service.payload.ApiResponse;
import com.holdings.server.service.payload.RefreshConfirmationTokenRequest;
import com.holdings.server.service.payload.UserAccountCreationRequest;
import com.holdings.server.service.repository.ConfirmationTokenRepository;
import com.holdings.server.service.repository.UserAccountRepository;
import com.holdings.server.service.utility.IpUtils;
import com.holdings.server.service.utility.ValueValidate;

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
	private MailContentBuilder mailContentBuilder;

	@Autowired
	private EmailSenderService emailSenderService;

	@Value("${spring.mail.username}")
	private String emailFrom;

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

		// Confirmation Mail
		ConfirmationToken token = new ConfirmationToken(user);
		while (confirmationTokenRepository.existsByConfirmationToken(token.getConfirmationToken())) {
			token.freshToken();
		}
		confirmationTokenRepository.save(token);

		URI locationConfirm = ServletUriComponentsBuilder
				.fromUriString(serviceConfig.getClientWebDomain() + "/user/confirm/" + token.getConfirmationToken())
				.buildAndExpand().encode().toUri();

		String[] mailList = { newUser.getEmail() };
		String mailContent = mailContentBuilder.generateMailContent(locationConfirm.toString(), Template.SIGN_UP);

		emailSenderService.sendComplexEmail(mailList, emailFrom,
				"Complete Registration! You need to confirm the e-mail for full functions.", mailContent);

		HashMap<String, Object> res = new HashMap<>();
		res.put("ID", newUser.getId());
		res.put("UserName", newUser.getUserName());

		return ResponseEntity.created(null).body(new ApiResponse(true, "Registe user account successfully.", res));
	}

	@PostMapping(value = { "/v{version:\\d}/user/confirm/refresh" })
	public ResponseEntity<?> userConfirmRefresh(@Valid @RequestBody RefreshConfirmationTokenRequest request)
			throws Exception {
		Optional<UserAccount> user = userAccountRepository.findByUserName(request.getUserName());

		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account name not found.");
		}

		if (user.get().getEmailVerified()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account had been verified.");
		}

		Optional<ConfirmationToken> token = confirmationTokenRepository.findByUser(user.get());

		if (!token.isPresent()) {
			ConfirmationToken ntoken = new ConfirmationToken(user.get());
			while (confirmationTokenRepository.existsByConfirmationToken(ntoken.getConfirmationToken())) {
				ntoken.freshToken();
			}
			confirmationTokenRepository.save(ntoken);

			return ResponseEntity.ok().body(new ApiResponse(true, "User confirmation token refreshed successfully."));
		}

		token.get().freshToken();
		int retry = 0;
		// Try 5 times.
		while (confirmationTokenRepository.existsByConfirmationToken(token.get().getConfirmationToken())) {
			token.get().freshToken();

			if (retry >= 6) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Refresh confirmation token failed. Try again.");
			}

			retry++;
		}

		confirmationTokenRepository.save(token.get());

		URI locationConfirm = ServletUriComponentsBuilder
				.fromUriString(
						serviceConfig.getClientWebDomain() + "/user/confirm/" + token.get().getConfirmationToken())
				.buildAndExpand().encode().toUri();

		String[] mailList = { user.get().getEmail() };
		String mailContent = mailContentBuilder.generateMailContent(locationConfirm.toString(), Template.CONFIRM_AGAIN);

		emailSenderService.sendComplexEmail(mailList, emailFrom,
				"Refresh confirmation token completed, confirm again !", mailContent);

		return ResponseEntity.ok().body(new ApiResponse(true, "User confirmation token refreshed successfully."));
	}

	@GetMapping(value = { "/v{version:\\d}/user/confirm/{token:\\d{6,6}}" })
	public ResponseEntity<?> userConfirmToken(@PathVariable String version, @Valid @PathVariable("token") String token) throws Exception {
		Optional<ConfirmationToken> ctoken = confirmationTokenRepository.findByConfirmationToken(token);
		if (!ctoken.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid confirmation token.");
		}

		Optional<UserAccount> user = userAccountRepository.findById(ctoken.get().getUser().getId());
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user.");
		}

		if (user.get().getEmailVerified()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account had been verified.");
		}

		user.get().setEmailVerified(true);
		userAccountRepository.save(user.get());

		HashMap<String, Object> res = new HashMap<>();
		res.put("ID", user.get().getId());
		res.put("UserName", user.get().getUserName());

		return ResponseEntity.ok().body(new ApiResponse(true, "User confirm successfully.", res));
	}
}
