package com.zeedoo.api.users.domain;

import com.google.common.base.Objects;

public class User {
	
	private String username;
	
	private String password;
	
	private String nickname;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("username", username).add("password", password).add("nickname", nickname).toString();
	}
	

}
