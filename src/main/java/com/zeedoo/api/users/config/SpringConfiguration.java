package com.zeedoo.api.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zeedoo.api.users.health.AppHealthCheck;
import com.zeedoo.api.users.service.AppService;
import com.zeedoo.api.users.tasks.AppTask;

/**
 * Represents Spring Configuration
 */
@Configuration
@ImportResource({"classpath:applicationContext.xml"})
public class SpringConfiguration {
	
	@Value("${config.env}")
	private String env;

	@Value("${config.message}")
	@JsonProperty
	private String message;

	@Bean
	public AppService appService() {
		return new AppService(message);
	}

	@Bean
	public AppTask appTask() {
		return new AppTask();
	}

	@Bean
	public AppHealthCheck appHealthCheck() {
		return new AppHealthCheck();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}	
}
