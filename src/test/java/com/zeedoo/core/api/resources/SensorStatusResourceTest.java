package com.zeedoo.core.api.resources;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.core.api.App;
import com.zeedoo.core.api.DropwizardJunitRunner;
import com.zeedoo.core.api.ServiceConfiguration;
import com.zeedoo.core.api.TestConstants;

@RunWith(DropwizardJunitRunner.class)
@ServiceConfiguration(value = App.class, setting = TestConstants.TEST_YAML_CONFIG)
public class SensorStatusResourceTest extends BaseResourceTest {
	
	public SensorStatusResourceTest() throws Exception {
	}
	
	@Test
	public void postSensorStatus() {
		SensorStatus testStatus = new SensorStatus();
		testStatus.setSensorId("Galaxy");
		testStatus.setLastUpdated(null);
		testStatus.setSensorStatus(DeviceStatus.ONLINE);
		WebResource webResource = client
				.resource("http://localhost:9898/sensorStatus");
		SensorStatus newStatus = webResource.type(MediaType.APPLICATION_JSON)
				.post(SensorStatus.class, testStatus);
		Assert.assertEquals(testStatus.getSensorId(), newStatus.getSensorId());
	}

}
