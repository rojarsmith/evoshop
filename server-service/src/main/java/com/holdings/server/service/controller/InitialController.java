package com.holdings.server.service.controller;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.holdings.server.service.ServiceConfig;
import com.holdings.server.service.entity.Permission;
import com.holdings.server.service.entity.Role;
import com.holdings.server.service.entity.UserAccount;
import com.holdings.server.service.repository.PermissionRepository;
import com.holdings.server.service.repository.RoleRepository;
import com.holdings.server.service.repository.UserAccountRepository;

@RestController
public class InitialController {
	@Autowired
	private ServiceConfig serviceConfig;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	private void init() {
		if (serviceConfig.isDev()) {
			initAccount();
			initRolePermission();
		} else if (serviceConfig.isTest()) {
			initAccountForTest();
		}
	}

	// Dev
	public boolean initRolePermission() {
		// Must begin with ROLE_***
		Role role = new Role("ROLE_ADMIN", "Admin");
		List<Permission> permissionList = permissionRepository.findAll();
		role.setPermissionList(permissionList);
		roleRepository.save(role);

		Role roleMember = new Role("ROLE_MEMBER", "Member");
		List<String> search = List.of("ACCOUNT_DETAIL_PANEL", "GUEST_AREA");
		List<Permission> memberPermissionList = permissionRepository.findBySymbolIn(search);
		roleMember.setPermissionList(memberPermissionList);
		roleRepository.save(roleMember);

		Role managerRole = new Role("ROLE_MANAGER", "Manager");
		List<String> managerSearch = List.of("ACCOUNT_DETAIL_PANEL", "GUEST_AREA");
		List<Permission> managerPermissions = permissionRepository.findBySymbolIn(managerSearch);
		managerRole.setPermissionList(managerPermissions);
		roleRepository.save(managerRole);

		Role role2 = new Role("ROLE_GUEST", "Guest");
		Optional<Permission> permissionList2 = permissionRepository.findBySymbol("GUEST_AREA");
		List<Permission> list = Arrays.asList(permissionList2.get());
		role2.setPermissionList(list);
		roleRepository.save(role2);

		return true;
	}

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

	public boolean initAccountForTest() {
		try {
			  
			// Add user.
			Instant instant = Instant.parse("2021-01-02T03:04:05.06Z");
			
			UserAccount user = new UserAccount();
			user.setUserName("ANEXTSOFT");
			user.setPassword(passwordEncoder.encode("Password"));
			user.setEmail("service@anextsoft.com");
			user.setEmailVerified(true);
			user.setRegisterTime(instant);
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
