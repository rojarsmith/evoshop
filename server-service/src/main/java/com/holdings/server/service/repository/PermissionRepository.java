package com.holdings.server.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.holdings.server.service.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Optional<Permission> findByName(String name);

	List<Permission> findByNameIn(List<String> names);
}
