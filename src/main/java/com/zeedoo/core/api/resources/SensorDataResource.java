package com.zeedoo.core.api.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.yammer.dropwizard.jersey.params.DateTimeParam;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.commons.domain.SensorDataRecords;
import com.zeedoo.core.api.dao.SensorDataRecordsDao;

@Path("/sensorData")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SensorDataResource {
	
	@Autowired
	private SensorDataRecordsDao sensorDataRecordsDao;
	
	@Path("/{sensorId}")
	@GET
	@Timed
	public SensorDataRecords doGetSensorData(@PathParam("sensorId") String sensorId, 
			@QueryParam("startDate") DateTimeParam startDate, @QueryParam("endDate") DateTimeParam endDate) {
		if (sensorId == null) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provide a valid sensor Id!").build();
			throw new WebApplicationException(response);
		}
		DateTime start = startDate != null ? startDate.get() : null;
		DateTime end = endDate != null ? endDate.get() : null;
		List<SensorDataRecord> records = sensorDataRecordsDao.get(sensorId, start, end);
		SensorDataRecords result = new SensorDataRecords(records);
		return result;
	}

}
