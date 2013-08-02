package com.zeedoo.core.api.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.core.api.dao.SensorStatusDao;

/**
 * Resource endpoint to work with sensor status
 * Core Sensor Info 
 * @author nzhu
 *
 */
@Path("/sensorStatus")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SensorStatusResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorStatusResource.class);
	
	@Autowired
	private SensorStatusDao sensorStatusDao;

	@Path("/{sensorId}")
	@GET
	@Timed
	public SensorStatus doGet(@PathParam("sensorId") String sensorId) {
		Optional<SensorStatus> sensorStatus = Optional.<SensorStatus>fromNullable(sensorStatusDao.get(sensorId));
		if (sensorStatus.isPresent()) {
			return sensorStatus.get();
		} else {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Could not find sensorStatus with sensorId: " + sensorId).build());
		}
	}
	
	@POST
	@Timed
	public SensorStatus doPost(@Valid SensorStatus sensorStatus) {
		int result = sensorStatusDao.insert(sensorStatus);
		if (result == 0) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to create sensorStatus=" + sensorStatus).build());
		}
		return sensorStatusDao.get(sensorStatus.getSensorId());
	}
	
	@Path("/{sensorId}")
	@PUT
	@Timed
	public SensorStatus doPut(@PathParam("sensorId") String sensorId, @Valid SensorStatus sensorStatus) {
		if (!sensorId.equals(sensorStatus.getSensorId())) {
			LOGGER.warn("Path param sensorId={} does NOT match the sensorId in the SensorStatus entity={}, using the sensorId in the entity instead", sensorId, sensorStatus);
		}
		int result = sensorStatusDao.update(sensorStatus);
		if (result == 0) {
			LOGGER.warn("DELETE operation did not affect any entities. SensorId={}", sensorId);
		}
		return sensorStatusDao.get(sensorStatus.getSensorId());
	}
	
	@Path("/{sensorId}")
	@DELETE
	@Timed
	public Response doDelete(@PathParam("sensorId") String sensorId) {
		int result = sensorStatusDao.delete(sensorId);
		if (result == 0) {
			LOGGER.warn("DELETE operation did not affect any entities. SensorId={}", sensorId);
		}
		return Response.status(Status.OK).build();
	}
}
