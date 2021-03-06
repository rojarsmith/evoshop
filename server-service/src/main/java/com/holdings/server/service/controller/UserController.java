package com.holdings.server.service.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.holdings.server.service.ServiceConfig;
import com.holdings.server.service.entity.ConfirmationToken;
import com.holdings.server.service.entity.ResetPassword;
import com.holdings.server.service.entity.UserAccount;
import com.holdings.server.service.payload.ApiResponse;
import com.holdings.server.service.payload.ForgetPasswordRequest;
import com.holdings.server.service.payload.RefreshConfirmationTokenRequest;
import com.holdings.server.service.payload.ResetPasswordRequest;
import com.holdings.server.service.payload.UserAccountAuthenticationRequest;
import com.holdings.server.service.payload.UserAccountCreationRequest;
import com.holdings.server.service.repository.ConfirmationTokenRepository;
import com.holdings.server.service.repository.ResetPasswordRepository;
import com.holdings.server.service.repository.UserAccountRepository;
import com.holdings.server.service.service.EmailSenderService;
import com.holdings.server.service.service.MailContentBuilder;
import com.holdings.server.service.service.MailContentBuilder.Template;
import com.holdings.server.service.utility.IpUtils;
import com.holdings.server.service.utility.JwtUtil;
import com.holdings.server.service.utility.SHA2;
import com.holdings.server.service.utility.ValueValidate;

@RestController
@RequestMapping(value = { "/api" })
public class UserController {
	@Autowired
	private ServiceConfig serviceConfig;

	@Autowired
	private ResetPasswordRepository resetPasswordRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

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
	public ResponseEntity<?> userConfirmToken(@PathVariable String version, @Valid @PathVariable("token") String token)
			throws Exception {
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

	@PostMapping(value = { "/v{version:\\d}/user/authentication/token" })
	public ResponseEntity<?> userAuthenticationToken(@RequestHeader(value = "Authorization") String headerData)
			throws Exception {
		UserAccountAuthenticationRequest authenticationRequest = new UserAccountAuthenticationRequest();
		String[] data = headerData.split(" ");
		byte[] decoded = Base64.getDecoder().decode(data[1]);
		String decodedStr = new String(decoded, StandardCharsets.UTF_8);
		data = decodedStr.split(":");

		authenticationRequest.setUsername(data[0]);
		authenticationRequest.setPassword(data[1]);

		Authentication ar = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
				authenticationRequest.getPassword());

		try {
			ar = authenticationManager.authenticate(ar);
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect username or password.");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username does not exist.");
		}

		final UserDetails userDetails = (UserDetails) ar.getPrincipal();

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		Instant t = jwtTokenUtil.extractExpiration(jwt).toInstant();
		HashMap<String, Object> res = new HashMap<>();
		res.put("Token", jwt);
		res.put("Expiration", t);

		return ResponseEntity.ok().body(new ApiResponse(true, "Create user authentication tokenconfirmation token successfully.", res));
	}

	@PostMapping(value = { "/v{version:\\d}/user/password/forget" })
	public ResponseEntity<?> userPasswordForget(@Valid @RequestBody ForgetPasswordRequest request) throws Exception {
		Optional<UserAccount> user = userAccountRepository.findByEmail(request.getEmail());
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found.");
		}

		List<ResetPassword> rps = resetPasswordRepository.findByUserId(user.get().getId());
		if (rps.size() >= 1) {
			for (ResetPassword rp : rps) {
				rp.setActive(false);
			}
			resetPasswordRepository.saveAll(rps);
		}

		Instant now = Instant.now();
		String token = SHA2.getSHA512(now.toString()).substring(0, 16);

		int retry = 0;
		while (resetPasswordRepository.existsByToken(token)) {
			token = SHA2.getSHA512(Instant.now().toString()).substring(0, 16);

			if (retry >= 6) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ask reset password failed. Try again.");
			}
			retry++;
		}

		ResetPassword resetPassword = new ResetPassword();
		resetPassword.setActive(true);
		resetPassword.setUserId(user.get().getId());
		resetPassword.setTime(now);
		resetPassword.setToken(token);
		resetPasswordRepository.save(resetPassword);

		URI location = ServletUriComponentsBuilder
				.fromUriString(serviceConfig.getClientWebDomain() + "/user/password/reset/" + token).buildAndExpand()
				.encode().toUri();

		String[] mailList = { user.get().getEmail() };
		String mailContent = mailContentBuilder.generateMailContent(location.toString(), Template.RESET_PASSWORD);
		emailSenderService.sendComplexEmail(mailList, emailFrom,
				"Begin Reset password! You need to click the link for next step.", mailContent);

		HashMap<String, String> res = new HashMap<>();
		res.put("token", token);

		return ResponseEntity.ok().body(new ApiResponse(true, "Ask reset password successfully.", res));
	}

	@PostMapping(value = { "/v{version:\\d}/user/password/reset" })
	public ResponseEntity<?> userPasswordReset(@Valid @RequestBody ResetPasswordRequest request) {
		if (!ValueValidate.validatePassword(request.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password validate failed.");
		}

		List<ResetPassword> tokens = resetPasswordRepository.findByToken(request.getToken());
		if (tokens.size() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token.");
		}

		Optional<ResetPassword> token = tokens.stream().filter(e -> e.getActive().equals(true)).findFirst();

		if (token.get() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Non actived token.");
		}

		Optional<UserAccount> user = userAccountRepository.findById(token.get().getUserId());
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Orphan token.");
		}

		token.get().setActive(false);
		resetPasswordRepository.save(token.get());
		user.get().setPassword(passwordEncoder.encode(request.getPassword()));
		userAccountRepository.save(user.get());

		return ResponseEntity.ok().body(new ApiResponse(true, "Reset password successfully."));
	}

	@GetMapping(value = { "/v{version:\\d}/user/current" })
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_MEMBER')")
	public ResponseEntity<?> userCurrent() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		String name = userDetails.getUsername();

		Optional<UserAccount> user = userAccountRepository.findByUserName(name);
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found the user.");
		}

		return ResponseEntity.ok().body(new ApiResponse(true, "Get the current user successfully.", user.get()));
	}
}
