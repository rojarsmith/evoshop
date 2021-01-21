package com.holdings.server.service.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "permission", uniqueConstraints = { @UniqueConstraint(columnNames = "symbol") })
public class Permission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4528131665337624512L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Permission symbol
	 */
	@NotNull
	private String symbol;

	/**
	 * Permission info
	 */
	private String info;

	public Permission() {
	}

	public Permission(@NotNull String symbol, String info) {
		this.symbol = symbol;
		this.info = info;
	}
}
