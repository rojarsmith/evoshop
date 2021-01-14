package com.holdings.server.service.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.holdings.server.service.entity.UserAccount;
import com.holdings.server.service.repository.UserAccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserAccount> user = userAccountRepository.findByUserName(username);

		if (!user.isPresent()) {
			throw new UsernameNotFoundException(username + " not found");
		}

		System.out.println("UserName=" + user.get().getUserName());
		return new User(user.get().getUserName(), user.get().getPassword(), Arrays.asList());
	}

}
