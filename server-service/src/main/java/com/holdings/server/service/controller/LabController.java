package com.holdings.server.service.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.holdings.server.service.payload.ApiResponse;

@RestController
@RequestMapping(value = { "/api" })
public class LabController {
	public ResponseEntity<?> lab1(HttpServletRequest request) throws Exception {
		HashMap<String, Object> res = new HashMap<>();
		res.put("Column1", 123);

		return ResponseEntity.created(null).body(new ApiResponse(true, "Lab1.", res));
	}

	public ResponseEntity<?> lab2(HttpServletRequest request) throws Exception {
		HashMap<String, Object> res = new HashMap<>();
		res.put("Column1", 123);

		return ResponseEntity.created(null).body(new ApiResponse(true, "Lab1.", res));
	}
	
	@GetMapping(value = { "/v{version:\\d}/lab3" })
	public ResponseEntity<?> lab3(HttpServletRequest request) throws Exception {
		HashMap<String, Object> res = new HashMap<>();
		res.put("Column1", 123);

		return ResponseEntity.ok(new ApiResponse(true, "Lab1.", res));
	}
}
