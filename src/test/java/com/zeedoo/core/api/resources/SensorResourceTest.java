package com.zeedoo.core.api.resources;

import java.util.Random;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;

import com.sun.jersey.api.client.WebResource;
import com.zeedoo.commons.domain.Sensor;

public class SensorResourceTest extends BaseResourceTest {
	
	public SensorResourceTest() throws Exception {
		super();
	}
	
	public void runTests() {
		putSensorStatus();
	}
	
	public void putSensorStatus() {
		WebResource webResource = client
				.resource("http://localhost:9898/sensor" + "/S1");
		Sensor testSensor = webResource.accept(MediaType.APPLICATION_JSON).get(Sensor.class);
		Random generator = new Random();
		Integer randomPort = generator.nextInt(100) + 1;
		testSensor.setSunIpPort(randomPort);
		testSensor = webResource.type(MediaType.APPLICATION_JSON)
				.put(Sensor.class, testSensor);
		Assert.assertEquals(randomPort, testSensor.getSunIpPort());
	}

}
