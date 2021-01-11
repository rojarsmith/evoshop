package com.holdings.serverservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.holdings.serverservice.entity.ConfirmationToken;
import com.holdings.serverservice.entity.UserAccount;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {
	Optional<ConfirmationToken> findByUser(UserAccount user);

	Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);

	Boolean existsByConfirmationToken(String confirmationToken);
}
