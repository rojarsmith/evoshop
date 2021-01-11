package com.holdings.server.service.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
public class ConfirmationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "confirmation_token")
	private String confirmationToken;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@OneToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(nullable = false, name = "user_id")
	private UserAccount user;

	public ConfirmationToken() {
	}

	public ConfirmationToken(UserAccount user) {
		this.user = user;
		createdDate = new Date();
		freshToken();
	}

	public void freshToken() {
		// confirmationToken = UUID.randomUUID().toString(); // Too long.
		Long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
		confirmationToken = uuid.toString().substring(uuid.toString().length() - 6, uuid.toString().length());
		createdDate = new Date();
	}
}
