package com.zeedoo.core.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.yammer.dropwizard.jersey.params.DateTimeParam;
import com.yammer.metrics.annotation.Timed;

@Path("/sensorData")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SensorDataResource {
	
	@Path("/{sensorId}")
	@GET
	@Timed
	public String doGetSensorData(@PathParam("sensorId") String sensorId, 
			@QueryParam("startDate") DateTimeParam startDate, @QueryParam("endDate") DateTimeParam endDate) {
		return sensorId + startDate + endDate;
	}

}
