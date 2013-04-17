package com.zeedoo.api.users.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * Represents DropWizard Configuration
 * 
 * @author nzhu
 * 
 */

public class AppConfiguration extends Configuration {
	
	@JsonProperty
	private SpringConfiguration config;

	public SpringConfiguration getSpringConfiguration() {
		return config;
	}

	public void setSpringConfiguration(SpringConfiguration config) {
		this.config = config;
	}

}
