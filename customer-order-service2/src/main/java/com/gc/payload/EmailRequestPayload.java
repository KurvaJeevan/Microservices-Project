package com.gc.payload;

import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailRequestPayload {

	private String email;
	private String subject;
	private String body;
	private LocalDateTime dateTime;
	private ZoneId zoneId;
}
