package com.zeedoo.core.api.resources;

import java.util.ArrayList;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.commons.domain.SensorDataRecords;
import com.zeedoo.core.api.database.dao.SensorDataRecordsDao;

public class SensorDataResourceTest extends BaseResourceTest {
	
	private final SensorDataRecordsDao sensorDataRecordsDao;

	public SensorDataResourceTest() throws Exception {
		super();
		sensorDataRecordsDao = new SensorDataRecordsDao();
		sensorDataRecordsDao.setDatabaseService(sqlService);
	}
	
	@Override
	public void runTests() {	
		testPutRecords();		
	}
	
	public void testPutRecords() {
		WebResource resource = client.resource("http://localhost:9898/sensorData");
	    ArrayList<SensorDataRecord> list = Lists.newArrayList();
	    list.add(new SensorDataRecord("testSensor", new DateTime().getMillis(), "1234"));
	    list.add(new SensorDataRecord("testSensor", new DateTime().plusMinutes(10).getMillis(), "1234"));
		SensorDataRecords records = new SensorDataRecords(list);
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, records);
		Assert.assertEquals(200, response.getStatus());
	}

}
