package com.zeedoo.core.api.resources;

import java.util.UUID;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.eclipse.jetty.server.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.zeedoo.core.api.App;
import com.zeedoo.core.api.DropwizardJunitRunner;
import com.zeedoo.core.api.ServiceConfiguration;
import com.zeedoo.core.api.TestConstants;
import com.zeedoo.core.api.client.HmacClientFilter;
import com.zeedoo.core.domain.User;
import com.zeedoo.core.domain.UserCredentials;

@RunWith(DropwizardJunitRunner.class)
@ServiceConfiguration(value = App.class, setting = TestConstants.TEST_YAML_CONFIG)
public class UsersResourceTest {
	
	private static final String TEST_USER_UUID = "5102e2a9-201c-4a26-8d69-7b8b93f85a55";
	private static final String TEST_USERNAME = "littlejedi";
	private static final String TEST_API_KEY = "dev";
	private static final String TEST_SECRET_KEY = "6fdd1400-a709-11e2-9e96-0800200c9a66";
	private Client client;
	
	@Before
	public void setUp() {
		client = Client.create();
		client.getProperties().put("api_key", TEST_API_KEY);
		client.getProperties().put("secret_key", TEST_SECRET_KEY);
		client.addFilter(new HmacClientFilter(client.getProviders()));
	}
	
	@Test
	public void testRegisterUser() {
		WebResource webResource = client
				.resource("http://localhost:9898/users/register");
		UUID uuid = UUID.randomUUID();
		String uuidNoDashes = uuid.toString().replace("-", "");
		User user = new User();
		user.setUsername(uuidNoDashes);
		user.setPassword("test123");
		user.setEmail("testEmail@blackhole.com");
		user.setUuid(uuid);
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, user);
		
		Assert.assertEquals(Response.SC_OK, response.getStatus());
	}
	
	@Test
	public void testGetUser() {
		// GET by username
		WebResource webResource = client
				.resource("http://localhost:9898/users").path(TEST_USERNAME);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		// test we get 200 back
		Assert.assertEquals(Response.SC_OK, response.getStatus());
		User user = response.getEntity(User.class);
		Assert.assertEquals("littlejedi", user.getUsername());
		
		// GET by uuid
		webResource = client
				.resource("http://localhost:9898/users").path(TEST_USER_UUID);
		response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		// test we get 200 back
		Assert.assertEquals(Response.SC_OK, response.getStatus());
        user = response.getEntity(User.class);	
		Assert.assertEquals("littlejedi", user.getUsername());
	}
	
	@Test
	public void testLogin() {
		WebResource webResource = client
				.resource("http://localhost:9898/users/login");
		UserCredentials credz = new UserCredentials("bf9ea89acbf042c88b3c37a1869f0cd7", "test123");		
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, credz);
		
		Assert.assertEquals(Response.SC_OK, response.getStatus());
	}
	
	@Test
	public void testLogout() {
		WebResource webResource = client
				.resource("http://localhost:9898/users/logout/" + "bf9ea89acbf042c88b3c37a1869f0cd7");	
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class);
		
		Assert.assertEquals(Response.SC_OK, response.getStatus());
	}
	
	
	@Test
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
