package com.gc.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean("asynctaskexecutor")
	Executor asyncTaskExecutor() {
		ThreadPoolTaskExecutor executor= new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setQueueCapacity(150);
		executor.setMaxPoolSize(4);
		executor.setThreadNamePrefix("AsyncTaskThread-");
		executor.initialize();
		return executor;
	}
}
