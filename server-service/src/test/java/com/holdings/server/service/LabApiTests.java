package com.holdings.server.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class LabApiTests {
	protected MockMvc mockMvc;

	@Autowired
	protected WebApplicationContext wac;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void testLab3() throws Exception {
		String responseString = mockMvc.perform(
				get("http://localhost:3020/api/v1/lab3")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("pcode", "root")
		).andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
		System.out.println("--------返回的json = " + responseString);

	}
}
