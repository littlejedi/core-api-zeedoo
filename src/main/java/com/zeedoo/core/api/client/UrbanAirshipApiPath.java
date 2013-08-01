package com.zeedoo.core.api.client;

public enum UrbanAirshipApiPath {
	
	DEVICE_TOKENS("device_tokens"),
	APIDS("apids"),
	PUSH("push");
	
	private String name;
	
	UrbanAirshipApiPath(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
