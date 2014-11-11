package com.zeedoo.core.api.resources;

import java.util.UUID;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.eclipse.jetty.server.Response;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.zeedoo.commons.domain.User;
import com.zeedoo.commons.domain.UserCredentials;

public class UsersResourceTest extends BaseResourceTest  {

	private static final String TEST_USERNAME = "littlejedi";

	public UsersResourceTest() throws Exception {
		super();
	}
	
	@Override
	public void runTests() {		
		testRegisterUser();
		testGetUser();
		testLogin();
		testLogout();
		testUpdateUser();
	}
	
	public void testRegisterUser() {
		WebResource webResource = client
				.resource("http://localhost:9898/users/register");
		UUID uuid = UUID.randomUUID();
		String uuidNoDashes = uuid.toString().replace("-", "");
		User user = new User();
		user.setUsername(uuidNoDashes);
		user.setPassword("test123");
		user.setEmail("testEmail@blackhole.com");
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, user);
		
		Assert.assertEquals(Response.SC_OK, response.getStatus());
	}
		
	public void testGetUser() {
		// GET by username
		WebResource webResource = client
				.resource("http://localhost:9898/users").path(TEST_USERNAME);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		// test we get 200 back
		Assert.assertEquals(Response.SC_OK, response.getStatus());
		User user = response.getEntity(User.class);
		Assert.assertEquals("littlejedi", user.getUsername());
	}
	
	public void testLogin() {
		WebResource webResource = client
				.resource("http://localhost:9898/users/login");
		UserCredentials credz = new UserCredentials("bf9ea89acbf042c88b3c37a1869f0cd7", "test123");		
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, credz);
		
		Assert.assertEquals(Response.SC_OK, response.getStatus());
	}
	
	public void testLogout() {
		WebResource webResource = client
				.resource("http://localhost:9898/users/logout/" + "bf9ea89acbf042c88b3c37a1869f0cd7");	
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class);
		
		Assert.assertEquals(Response.SC_OK, response.getStatus());
	}
	
	public void testUpdateUser() {
		WebResource webResource = client
				.resource("http://localhost:9898/users").path(TEST_USERNAME);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		// test we get 200 back
		Assert.assertEquals(Response.SC_OK, response.getStatus());
		User user = response.getEntity(User.class);
		String oldEmail = user.getEmail();
		user.setEmail("newEmail@blackhole");
		response = webResource.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, user);
		User updatedUser = response.getEntity(User.class);
		Assert.assertEquals(Response.SC_OK, response.getStatus());
		Assert.assertEquals("newEmail@blackhole", updatedUser.getEmail());
		// revert to original
		updatedUser.setEmail(oldEmail);
		response = webResource.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, user);
		Assert.assertEquals(Response.SC_OK, response.getStatus());
	}

}
