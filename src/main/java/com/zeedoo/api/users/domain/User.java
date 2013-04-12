package com.zeedoo.api.users.domain;

import java.util.UUID;

import com.google.common.base.Objects;

public class User {
	
	//TODO: Make this read-only / add read only annotation
	private UUID uuid;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String nickname;
	
	private String secretKey;
	
	public String getSecretKey() {
		return secretKey;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("email", email).add("username", username).add("nickname", nickname).add("uuid", uuid).toString();
	}
}
