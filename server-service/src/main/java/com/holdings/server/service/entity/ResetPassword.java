package com.holdings.server.service.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "reset_password")
public class ResetPassword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Boolean active;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Instant time;

	@Column(nullable = false)
	private String token;
}
