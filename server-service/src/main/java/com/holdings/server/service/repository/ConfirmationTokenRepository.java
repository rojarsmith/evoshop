package com.holdings.server.service.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.holdings.server.service.entity.ConfirmationToken;
import com.holdings.server.service.entity.UserAccount;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {
	Optional<ConfirmationToken> findByUser(UserAccount user);

	Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);

	Boolean existsByConfirmationToken(String confirmationToken);
}
