package com.holdings.server.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.holdings.server.service.entity.UserAccount;
import com.holdings.server.service.repository.UserAccountRepository;

@SpringBootTest
class UserAccountDaoTests {
    @Autowired
    private UserAccountRepository userAccountRepository;
	
	@Test
	void testFindByUserName() {
		Optional<UserAccount> user = userAccountRepository.findByUserName("ANEXTSOFT");
		assertEquals(user.get().getUserName(), "ANEXTSOFT");
	}

	@Test
	void testFindByEmail() {
		Optional<UserAccount> user = userAccountRepository.findByEmail("service@anextsoft.com");
		assertEquals(user.get().getEmail(), "service@anextsoft.com");
	}
}
