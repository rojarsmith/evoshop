package com.holdings.server.service.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.holdings.server.service.ServiceConfig;
import com.holdings.server.service.payload.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
	@Autowired
	private ServiceConfig serviceConfig;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			String message = "Unknown error.";
			if (exception instanceof ResponseStatusException) {
				message = ((ResponseStatusException) exception).getReason();
			} else if (exception instanceof ExpiredJwtException) {
				message = ((ExpiredJwtException) exception).getMessage();
			}

			System.out.println("[handleException]: " + message);

			ApiResponse resp = new ApiResponse(false, message);
			if (serviceConfig.isDev()) {
				resp = new ApiResponse(message, exception);
			}

			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().write(convertObjectToJson(resp));
		}
	}

	public String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}
