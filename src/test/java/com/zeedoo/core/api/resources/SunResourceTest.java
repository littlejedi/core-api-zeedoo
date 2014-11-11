package com.zeedoo.core.api.resources;

import java.util.Random;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;

import com.google.common.net.InetAddresses;
import com.sun.jersey.api.client.WebResource;
import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.Sun;
import com.zeedoo.core.api.database.dao.SunDao;

public class SunResourceTest extends BaseResourceTest {
	
	private SunDao sunDao;

	public SunResourceTest() throws Exception {
		super();
		sunDao = new SunDao();
		sunDao.setDatabaseService(sqlService);
	}
	
	@Override
	public void runTests() {		
		testCreate();
		testUpdate();
	}
	
	public void testCreate() {
		Integer fakePort = new Random().nextInt(1000);
		String fakeIpAddress = InetAddresses.fromInteger(new Random().nextInt()).getHostAddress();
		Sun sun = new Sun();
		sun.setCurrentIpAddress(fakeIpAddress);
		sun.setCurrentPort(fakePort);
		sun.setStatus(DeviceStatus.OFFLINE);
		WebResource resource = client.resource("http://localhost:9898/sun");
		Sun response = resource.type(MediaType.APPLICATION_JSON).post(Sun.class, sun);
		Assert.assertNotNull(response);
	}
	
	public void testUpdate() {
		String sunId = "1";
		// get
		WebResource resource = client.resource("http://localhost:9898/sun" + "/id" + "/" + sunId);
		Sun sun = resource.accept(MediaType.APPLICATION_JSON).get(Sun.class);
		Assert.assertNotNull(sun);
		String randomId = UUID.randomUUID().toString();
		sun.setSunSsid(randomId);
		// update
		resource = client.resource("http://localhost:9898/sun");
		resource.type(MediaType.APPLICATION_JSON).put(sun);
		// verify
		resource = client.resource("http://localhost:9898/sun" + "/id" + "/" + sunId);
		sun = resource.accept(MediaType.APPLICATION_JSON).get(Sun.class);
		Assert.assertNotNull(sun);
		Assert.assertEquals(randomId, sun.getSunSsid());	
	}

}
