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
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(uuid, username, password, email, nickname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equal(this.uuid, other.uuid) 
				&& Objects.equal(this.email, other.email)
				&& Objects.equal(this.username, other.username)
				&& Objects.equal(this.password, other.password)
				&& Objects.equal(this.nickname, other.nickname);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("email", email).add("username", username).add("nickname", nickname).add("uuid", uuid).toString();
	}
}
