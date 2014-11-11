package com.zeedoo.core.api.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.domain.SensorType;
import com.zeedoo.core.api.database.dao.SensorTypesDao;

/**
 * Resource to get all sensor types
 * @author nzhu
 *
 */
@Path("/sensorTypes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Component
public class SensorTypesResource {
	
	@Autowired
	private SensorTypesDao sensorTypesDao;

	@GET
	@Timed
	@CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
	public List<SensorType> getSensorTypes() {
		return sensorTypesDao.getSensorTypes();
	}

}
