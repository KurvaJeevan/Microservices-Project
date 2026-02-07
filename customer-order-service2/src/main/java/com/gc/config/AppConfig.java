package com.gc.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@EnableCaching
public class AppConfig {

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder templateBuilder) {
		return templateBuilder.build();
	}

	@Bean
	ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(5);
		scheduler.setThreadNamePrefix("DynamicScheduler-");
		scheduler.initialize();
		return scheduler;
	}

	@Bean
	ModelMapper mapper() {
		return new ModelMapper();
	}
}
