package com.zeedoo.api.users.domain;

import com.google.common.base.Objects;

/**
 * Represents a user's login credentials
 * @author littlejedi
 *
 */
public class UserCredentials {
	
	private String username;
	
	private String password;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("username", username).toString();
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
		return Objects.equal(username, other.username) && Objects.equal(password, password);
	}
}
