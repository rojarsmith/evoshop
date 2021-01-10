package com.holdings.serverservice.controller;

import java.time.Instant;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.holdings.serverservice.entity.UserAccount;
import com.holdings.serverservice.repository.UserAccountRepository;

@RestController
public class InitialController {
    @Autowired
    private UserAccountRepository userAccountRepository;
    
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	private void init() {
		initAccount();
	}

	// Dev
	public boolean initAccount() {
		try {
			// Person 1
			UserAccount user = new UserAccount();
			user.setUserName("aa11");
			user.setPassword(passwordEncoder.encode("bbbb1111"));
			user.setEmail("rojarsmith@live.com");
			user.setEmailVerified(true);
			user.setRegisterTime(Instant.now());
			user.setRegisterIp("127.0.0.1");
			user.setActived(true);
			userAccountRepository.save(user);
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}
}
