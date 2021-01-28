package com.holdings.server.service.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "role", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class Role implements Serializable {
	private static final long serialVersionUID = -2496144486439878279L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;

	/**
	 * Role description
	 */
	@NotNull
	private String description;

	/**
	 * Role's permission
	 */
	@ManyToMany(targetEntity = Permission.class, fetch = FetchType.EAGER)
	private Collection<Permission> permissions = new ArrayList<>();

	public Role() {
	}

	public Role(@NotNull String name, @NotNull String description) {
		this.name = name;
		this.description = description;
	}
}
