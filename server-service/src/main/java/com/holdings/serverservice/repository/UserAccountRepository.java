package com.holdings.serverservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.holdings.serverservice.entity.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{

}
