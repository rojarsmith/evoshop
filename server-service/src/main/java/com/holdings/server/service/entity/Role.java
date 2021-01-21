package com.holdings.server.service.entity;

import java.io.Serializable;
import java.util.List;

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
@Table(name = "role", uniqueConstraints = { @UniqueConstraint(columnNames = "symbol") })
public class Role implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2496144486439878279L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Role symbol
	 */
	@NotNull
	private String symbol;

	/**
	 * Role info
	 */
	@NotNull
	private String info;

	/**
	 * Role's permission
	 */
	@ManyToMany(targetEntity = Permission.class, fetch = FetchType.LAZY)
	private List<Permission> permissionList;

	public Role() {
	}

	public Role(@NotNull String symbol, @NotNull String info) {
		this.symbol = symbol;
		this.info = info;
	}
}
