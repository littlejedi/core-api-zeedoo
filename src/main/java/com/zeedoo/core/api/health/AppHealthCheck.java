package com.zeedoo.core.api.health;

import com.yammer.metrics.core.HealthCheck;

public class AppHealthCheck extends HealthCheck {
	
	public AppHealthCheck() {
		super("health");
	}
	
	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}
