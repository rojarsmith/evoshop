package com.holdings.serverservice.payload;

import org.apache.commons.lang3.exception.ExceptionUtils;

import lombok.Data;

@Data
public class ApiResponse {
	private boolean complete;

	private String message;

	private Object data;

	private String trace;

	public ApiResponse(boolean success, String message, Object data, String trace) {
		this.complete = success;
		this.message = message;
		this.data = data;
		this.trace = trace;
	}

	public ApiResponse(boolean success, String message, Object data) {
		this(success, message, data, null);
	}
	
	public ApiResponse(boolean success, String message) {
		this(success, message, null);
	}

	public ApiResponse(String message, Exception exception) {
		this.complete = false;
		this.message = message;
		this.data = null;
		if (exception != null) {
			this.trace = ExceptionUtils.getStackTrace(exception);
		} else {
			this.trace = null;
		}
	}
}
