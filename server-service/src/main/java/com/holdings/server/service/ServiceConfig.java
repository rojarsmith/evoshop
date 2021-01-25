package com.holdings.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties
public class ServiceConfig {
	@Value("${spring.profiles.active}")
	private String active;

	@Value("${app.client.web.domain}")
	private String clientWebDomain;

	@Value("${app.service.domain}")
	private String serviceDomain;

	public boolean isDev() {
		if (active.toUpperCase().equals("DEV")) {
			return true;
		}

		return false;
	}
	
	public boolean isTest() {
		if (active.toUpperCase().equals("TEST")) {
			return true;
		}

		return false;
	}
}
