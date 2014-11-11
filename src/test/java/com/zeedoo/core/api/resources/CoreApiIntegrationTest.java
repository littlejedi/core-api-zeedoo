package com.zeedoo.core.api.resources;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.zeedoo.core.api.App;
import com.zeedoo.core.api.DropwizardJunitRunner;
import com.zeedoo.core.api.ServiceConfiguration;
import com.zeedoo.core.api.TestConstants;

@RunWith(DropwizardJunitRunner.class)
@ServiceConfiguration(value = App.class, setting = TestConstants.TEST_YAML_CONFIG)
public class CoreApiIntegrationTest {
	
	@Test
	public void testBMResource() throws Exception {
		BMResourceTest resourceTest = new BMResourceTest();
		resourceTest.runTests();
	}
	
	@Test
	public void testDeviceTokenResource() throws Exception {
		DeviceTokenResourceTest resourceTest = new DeviceTokenResourceTest();
		resourceTest.runTests();
	}
	
	@Test
	public void testSensorDataResource() throws Exception {
		SensorDataResourceTest resourceTest = new SensorDataResourceTest();
		resourceTest.runTests();
	}

	@Test
	public void testSensorResource() throws Exception {
		SensorResourceTest sensorResourceTest = new SensorResourceTest();
		sensorResourceTest.runTests();
	}
	
	@Test
	public void testSunResource() throws Exception {
		SunResourceTest resourceTest = new SunResourceTest();
		resourceTest.runTests();
	}
	
	@Test
	public void testUsersResource() throws Exception {
		UsersResourceTest usersResourceTest = new UsersResourceTest();
		usersResourceTest.runTests();
	}
}
