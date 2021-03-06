package com.holdings.server.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.holdings.server.service.ServiceConfig;
import com.holdings.server.service.payload.ApiResponse;

@RestControllerAdvice
public class ExceptionController {
	@Autowired
	private ServiceConfig serviceConfig;

	@ExceptionHandler()
	public ResponseEntity<?> handleException(Exception exception) {
		String message = exception.getMessage();
		if (exception instanceof ResponseStatusException) {
			message = ((ResponseStatusException) exception).getReason();
		} else if (exception instanceof MethodArgumentNotValidException) {
			message = ((MethodArgumentNotValidException) exception).getMessage();
		}

		System.out.println("[handleException]: " + message);

		ApiResponse resp = new ApiResponse(false, message);
		if (serviceConfig.isDev()) {
			resp = new ApiResponse(message, exception);
		}

		return ResponseEntity.badRequest().body(resp);
	}
}
