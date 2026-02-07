package com.gc.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.dto.ApiResponse;
import com.gc.payload.EmailRequestPayload;
import com.gc.service.SchedulerServiceQuartz;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(EmailSchedulerController.class)
class EmailSchedulerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private SchedulerServiceQuartz schedulerServiceQuartz;
	@Autowired
	private ObjectMapper mapper;
	@Mock
	private EmailRequestPayload emailRequest;

	@Test
	@DisplayName("Testing to Schedule Email - EmailSchedulerController")
	void testScheduleEmail() throws Exception {
		ApiResponse rs= new ApiResponse(true, "Email Scheduled Successfully",
				emailRequest, HttpStatus.OK.value(), Collections.emptyList());
		String jsonRequest=mapper.writeValueAsString(emailRequest);
		when(schedulerServiceQuartz.scheduleEmail(any(EmailRequestPayload.class))).thenReturn(rs);
		mockMvc.perform(post("/schedule/email")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Email Scheduled Successfully"))
				.andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()));
		
	}

}
