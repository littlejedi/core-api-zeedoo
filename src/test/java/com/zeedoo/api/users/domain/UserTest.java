package com.zeedoo.api.users.domain;

import static com.yammer.dropwizard.testing.JsonHelpers.*;
import static org.hamcrest.Matchers.*;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {
	
	@Test
	public void serializesToJSON() throws Exception {
		final User user = new User();
		user.setUuid(UUID.fromString("7e128cd0-a475-11e2-9e96-0800200c9a66"));
		user.setUsername("username");
		user.setPassword("password");
		user.setEmail("email");
		user.setNickname("nickname");
		Assert.assertThat("a User can be serialized to JSON", asJson(user), is(equalTo(jsonFixture("fixtures/user.json"))));
	}
	
	@Test
	public void deserializesFromJSON() throws Exception {
	    final User user = new User();
	    user.setUuid(UUID.fromString("7e128cd0-a475-11e2-9e96-0800200c9a66"));
		user.setUsername("username");
		user.setPassword("password");
		user.setEmail("email");
		user.setNickname("nickname");
	    Assert.assertThat("a Person can be deserialized from JSON",
	               fromJson(jsonFixture("fixtures/user.json"), User.class),
	               is(user));
	}

}
