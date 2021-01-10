package com.holdings.serverservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.holdings.serverservice.ServiceConfig;
import com.holdings.serverservice.payload.ApiResponse;

@RestControllerAdvice
public class ExceptionController {
	@Autowired
	private ServiceConfig serviceConfig;

	@ExceptionHandler()
	public ResponseEntity<?> handleException(Exception exception) {
		String message = "Unknown error.";
		if (exception instanceof ResponseStatusException) {
			message = ((ResponseStatusException) exception).getReason();
		}
		System.out.println("[handleException]: " + message);

		ApiResponse resp = new ApiResponse(false, message);
		if (serviceConfig.isDev()) {
			resp = new ApiResponse(message, exception);
		}

		return ResponseEntity.badRequest().body(resp);
	}
}
