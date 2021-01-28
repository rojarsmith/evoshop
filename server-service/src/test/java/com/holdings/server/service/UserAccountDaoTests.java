package com.holdings.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.holdings.server.service.entity.Permission;
import com.holdings.server.service.entity.Role;
import com.holdings.server.service.entity.UserAccount;
import com.holdings.server.service.repository.PermissionRepository;
import com.holdings.server.service.repository.RoleRepository;
import com.holdings.server.service.repository.UserAccountRepository;

@SpringBootTest
class UserAccountDaoTests {
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;

	@Test
	void testUserAccount() {
		Optional<UserAccount> user = userAccountRepository.findByUserName("ANEXTSOFT");
		assertEquals(user.get().getUserName(), "ANEXTSOFT");
		
		user = userAccountRepository.findByEmail("service@anextsoft.com");
		assertEquals(user.get().getEmail(), "service@anextsoft.com");
	}

	@Test
	void testRoleRepository() {
		Optional<Role> role = roleRepository.findByName("ROLE_ADMIN");
		assertThat(role).isNotEmpty();
		assertEquals(role.get().getDescription(), "Admin");
		assertThat(role.get().getPermissions()).isNotEmpty();
		Optional<Permission> permission = permissionRepository.findByName("READ_LAB1");
		assertThat(permission).isNotEmpty();
		assertEquals(permission.get().getDescription(), "Read lab1");
	}
}
