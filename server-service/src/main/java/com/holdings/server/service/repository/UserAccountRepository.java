package com.holdings.server.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.holdings.server.service.entity.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	Optional<UserAccount> findByUserName(String userName);

	Optional<UserAccount> findByEmail(String email);

	Boolean existsByUserName(String userName);

	Boolean existsByEmail(String email);
}
