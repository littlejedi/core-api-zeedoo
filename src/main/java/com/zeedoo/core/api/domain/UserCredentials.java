package com.zeedoo.core.api.domain;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.Objects;

/**
 * Represents a user's login credentials
 * 
 * @author littlejedi
 * 
 */
public class UserCredentials {

	@NotEmpty
	private String username;

	@NotEmpty
	private String password;
	
	public UserCredentials() {}

	public UserCredentials(String username, String password) {
		this.username = username;
		this.password = password;
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

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("username", username)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(username, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserCredentials other = (UserCredentials) obj;
		return Objects.equal(username, other.username)
				&& Objects.equal(password, password);
	}
}
