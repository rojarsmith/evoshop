package com.holdings.server.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.holdings.server.service.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findBySymbol(String symbol);

	List<Role> findBySymbolIn(List<String> symbols);
}
