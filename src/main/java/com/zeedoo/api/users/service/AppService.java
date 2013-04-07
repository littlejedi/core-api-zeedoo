package com.zeedoo.api.users.service;

public class AppService {
	
	private String message;

    public AppService(String message) {
        this.message = message;
    }

    public String greeting() {
        return message;
    }

    public String getMessage() {
        return message;
    }
}
