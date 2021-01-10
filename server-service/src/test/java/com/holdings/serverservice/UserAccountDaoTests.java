package com.holdings.serverservice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.holdings.serverservice.entity.UserAccount;
import com.holdings.serverservice.repository.UserAccountRepository;

@SpringBootTest
class UserAccountDaoTests {
    @Autowired
    private UserAccountRepository userAccountRepository;
	
	@Test
	void testFindByUserName() {
		Optional<UserAccount> user = userAccountRepository.findByUserName("aa11");
		assertEquals(user.get().getUserName(), "aa11");
	}

	@Test
	void testFindByEmail() {
		Optional<UserAccount> user = userAccountRepository.findByEmail("rojarsmith@live.com");
		assertEquals(user.get().getEmail(), "rojarsmith@live.com");
	}
}
