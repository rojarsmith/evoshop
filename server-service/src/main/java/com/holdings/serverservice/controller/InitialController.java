package com.holdings.serverservice.controller;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {
	   @PostConstruct
	    private void init() {
	   }
}
