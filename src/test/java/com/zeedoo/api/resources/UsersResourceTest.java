package com.zeedoo.api.resources;

import java.util.UUID;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.eclipse.jetty.server.Response;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.zeedoo.api.client.HmacClientFilter;
import com.zeedoo.api.users.App;
import com.zeedoo.api.users.DropwizardJunitRunner;
import com.zeedoo.api.users.ServiceConfiguration;
import com.zeedoo.api.users.domain.User;

@RunWith(DropwizardJunitRunner.class)
@ServiceConfiguration(value = App.class, setting = "src/main/resources/com/zeedoo/api/users/config/config.yml")
public class UsersResourceTest {
	
	private static final String TEST_USER_UUID = "5102e2a9-201c-4a26-8d69-7b8b93f85a55";
	private static final String TEST_USERNAME = "littlejedi";
	private static final String TEST_API_KEY = "dev";
	private static final String TEST_SECRET_KEY = "6fdd1400-a709-11e2-9e96-0800200c9a66";
	
	@Test
	public void testRegisterUser() {
		Client client = Client.create();
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
		Client client = Client.create();
		client.getProperties().put("api_key", TEST_API_KEY);
		client.getProperties().put("secret_key", TEST_SECRET_KEY);
		client.addFilter(new HmacClientFilter(client.getProviders()));
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
	
	//@Test
	//public void testLogin()

}
