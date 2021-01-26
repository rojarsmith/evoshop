package com.holdings.server.service.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Entity
@Data
@Table(name = "permission", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class Permission implements GrantedAuthority {
	private static final long serialVersionUID = -4528131665337624512L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;
	private String description;

	public Permission() {
	}

	public Permission(@NotNull String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public String getAuthority() {
		return name;
	}
}
