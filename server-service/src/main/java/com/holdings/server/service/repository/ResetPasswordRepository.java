package com.holdings.server.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holdings.server.service.entity.ResetPassword;

@Repository
public interface ResetPasswordRepository extends CrudRepository<ResetPassword, Long> {
	@Query(nativeQuery = true, value = "SELECT * FROM reset_password as e WHERE e.token = :token")
	List<ResetPassword> findByToken(@Param("token") String token);

	@Query(nativeQuery = true, value = "SELECT * FROM reset_password as e WHERE e.user_id = :userid")
	List<ResetPassword> findByUserId(@Param("userid") Long userid);

	Boolean existsByToken(String token);
}
