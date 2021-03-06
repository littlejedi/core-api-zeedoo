package com.zeedoo.core.api.resources;

import java.util.UUID;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.eclipse.jetty.server.Response;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.zeedoo.commons.domain.DeviceToken;
import com.zeedoo.core.api.database.dao.DeviceTokenDao;
import com.zeedoo.core.api.database.mapper.SqlService;

public class DeviceTokenResourceTest extends BaseResourceTest {
	
	protected final SqlService sqlService = new SqlService();
	private DeviceTokenDao deviceTokenDao = new DeviceTokenDao();

	private static final String TEST_DEVICE_TOKEN = "6E1FD6D3633470DCF01F0BDE690D99372A1C50788E5FB26833EC1E0B9F8DE39E";
	
	public DeviceTokenResourceTest() throws Exception {
		super();
		deviceTokenDao.setSqlService(sqlService);
	}
	
	@Override
	public void runTests() {		
		testRegisterDeviceToken();
		testUnregisterDeviceToken();
	}
	
	public void testRegisterDeviceToken() {
		WebResource webResource = client
				.resource("http://localhost:9898/deviceToken");
		DeviceToken token = new DeviceToken();
		token.setTokenId(TEST_DEVICE_TOKEN);
		token.setUsername(UUID.randomUUID().toString());
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, token);
		Assert.assertEquals(Response.SC_OK, response.getStatus());		
	}
	
	public void testUnregisterDeviceToken() {
		//DELETE has to call aysnc web resource
		WebResource webResource = client
				.resource("http://localhost:9898/deviceToken/" + TEST_DEVICE_TOKEN);
		ClientResponse response = webResource.delete(ClientResponse.class);
		Assert.assertEquals(Response.SC_OK, response.getStatus());	
		// Delete Token
	    deviceTokenDao.deleteDeviceToken(TEST_DEVICE_TOKEN);
	}
}
