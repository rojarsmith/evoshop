package com.holdings.serverservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.holdings.serverservice.entity.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	Optional<UserAccount> findByUserName(String userName);

	Optional<UserAccount> findByEmail(String email);

	Boolean existsByUserName(String userName);

	Boolean existsByEmail(String email);
}
