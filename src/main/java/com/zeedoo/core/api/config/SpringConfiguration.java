package com.zeedoo.core.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zeedoo.core.api.health.AppHealthCheck;
import com.zeedoo.core.api.service.AppService;
import com.zeedoo.core.api.tasks.AppTask;

/**
 * Represents Spring Configuration
 */
@Configuration
@ImportResource({"classpath:applicationContext.xml"})
public class SpringConfiguration {
	
	@Value("${config.env}")
	private String env;

	@Value("${config.message}")
	private String message;
		
	// Credentials to talk to UA RESTful API
	@Value("${config.urbanAirshipApiKey}")
	private String urbanAirshipApiKey;
	
	@Value("${config.urbanAirshipApiSecret}")
	private String urbanAirshipApiSecret;
	
	@Value("${config.urbanAirshipRootPath}")
	private String urbanAirshipRootPath;
	
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
	
	public String getUrbanAirshipApiKey() {
		return urbanAirshipApiKey;
	}

	public void setUrbanAirshipApiKey(String urbanAirshipApiKey) {
		this.urbanAirshipApiKey = urbanAirshipApiKey;
	}

	public String getUrbanAirshipApiSecret() {
		return urbanAirshipApiSecret;
	}

	public void setUrbanAirshipApiSecret(String urbanAirshipApiSecret) {
		this.urbanAirshipApiSecret = urbanAirshipApiSecret;
	}
	
	public String getUrbanAirshipRootPath() {
		return urbanAirshipRootPath;
	}

	public void setUrbanAirshipRootPath(String urbanAirshipRootPath) {
		this.urbanAirshipRootPath = urbanAirshipRootPath;
	}
}
