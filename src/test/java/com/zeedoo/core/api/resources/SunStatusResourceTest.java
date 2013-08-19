package com.zeedoo.core.api.resources;

import java.util.Random;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.net.InetAddresses;
import com.sun.jersey.api.client.WebResource;
import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.SunStatus;
import com.zeedoo.core.api.App;
import com.zeedoo.core.api.DropwizardJunitRunner;
import com.zeedoo.core.api.ServiceConfiguration;
import com.zeedoo.core.api.TestConstants;
import com.zeedoo.core.api.database.dao.SunStatusDao;

@RunWith(DropwizardJunitRunner.class)
@ServiceConfiguration(value = App.class, setting = TestConstants.TEST_YAML_CONFIG)
public class SunStatusResourceTest extends BaseResourceTest {

	private SunStatusDao sunStatusDao;
	
	public SunStatusResourceTest() throws Exception {
		super();
		sunStatusDao = new SunStatusDao();
		sunStatusDao.setDatabaseService(sqlService);
	}
	
	@Test
	public void testCreate() {
		String fakeId = UUID.randomUUID().toString();
		Integer fakePort = new Random().nextInt(1000);
		String fakeIpAddress = InetAddresses.fromInteger(new Random().nextInt()).getHostAddress();
		SunStatus status = new SunStatus(fakeIpAddress, fakePort, fakeId, DeviceStatus.ONLINE);
		WebResource resource = client.resource("http://localhost:9898/sunStatus");
		status = resource.type(MediaType.APPLICATION_JSON).post(SunStatus.class, status);
		Assert.assertNotNull(status);
		Assert.assertEquals(fakeId, status.getSunMacAddress());
	}
	
	@Test
	public void testUpdate() { 
		final String testIp = "112.151.147.222";
		final String testPort = "12345";
		WebResource resource = client.resource("http://localhost:9898/sunStatus" + "/" + testIp + ":" + testPort);
		SunStatus status = resource.accept(MediaType.APPLICATION_JSON).get(SunStatus.class);
		Random generator = new Random();
		String randomSunId = "Sun"  + generator.nextInt(100);
		Assert.assertNotNull(status);
		status.setSunMacAddress(randomSunId);
		status = client.resource("http://localhost:9898/sunStatus" + "/" + testIp + ":" + testPort).type(MediaType.APPLICATION_JSON).put(SunStatus.class, status);
		Assert.assertEquals(randomSunId, status.getSunMacAddress());
	}

}
