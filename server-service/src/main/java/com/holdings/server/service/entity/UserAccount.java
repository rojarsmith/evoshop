package com.holdings.server.service.entity;

import java.time.Instant;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class UserAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userName;

	@JsonIgnore
	private String password;

	@Email
	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private Boolean emailVerified = false;

	private Instant registerTime;

	private String registerIp;

	@Column(nullable = false)
	private Boolean actived = false;

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
	private Collection<Role> roleList;

	// region Privacy
	private String realName;

	private String company;

	private String job;

	private String taxcode;

	private String address;

	private String phone;
	// endregion
}
