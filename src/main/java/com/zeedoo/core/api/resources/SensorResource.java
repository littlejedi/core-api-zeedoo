package com.zeedoo.core.api.resources;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.FindByResult;
import com.zeedoo.commons.domain.Link;
import com.zeedoo.commons.domain.Paginator;
import com.zeedoo.commons.domain.Sensor;
import com.zeedoo.core.api.config.SpringConfiguration;
import com.zeedoo.core.api.constants.Constants;
import com.zeedoo.core.api.database.dao.SensorDao;
import com.zeedoo.core.api.utils.PaginatorUtils;

/**
 * Resource endpoint to work with Sensor info
 * @author nzhu
 *
 */
@Path("/sensor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SensorResource {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SensorResource.class);
	
	@Autowired
	private SensorDao sensorDao;
	
	@Autowired
	private SpringConfiguration configuration;

	@Path("/{sensorId}")
	@GET
	@Timed
	public Sensor doGet(@PathParam("sensorId") String sensorId) {
		Optional<Sensor> sensor = Optional.<Sensor>fromNullable(sensorDao.get(sensorId));
		if (sensor.isPresent()) {
			Sensor result = sensor.get();
			UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path(sensorId);
			result.setHref(builder.build().toASCIIString());
			return result;
		} else {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Could not find sensor with sensorId: " + sensorId).build());
		}
	}
	
	@Path("/findBy")
	@GET
	@Timed
	public Paginator findSensorsPaginated(@QueryParam("sunMacAddress") String sunMacAddress, @QueryParam("username") String username,
			@QueryParam("pageSize") Integer pageSize, @QueryParam("pageNumber") Integer pageNumber, @QueryParam("fullEntity") Boolean fullEntity) {
		int totalSensors = sensorDao.findSensorsCount(sunMacAddress, username);
		Paginator paginator = PaginatorUtils.fromResultAndPagingOptions(totalSensors, pageSize, pageNumber);
		int actualPageSize = paginator.getPageSize();
		int actualPageNumber = paginator.getPageNumber();
		int numberOfPages = paginator.getPageCount();
		boolean full = fullEntity != null ? fullEntity : Constants.SHOW_FULL_ENTITY;
		if (numberOfPages != 0 && paginator.getResultCount() != 0) {
			int start = (actualPageNumber - 1) * actualPageSize;
			int end = actualPageSize;
			if (full) {
				List<Sensor> sensors = sensorDao.findSensorsFullEntity(sunMacAddress, username, start, end);
				for (Sensor sensor : sensors) {
					this.resolveHrefLink(sensor);
				}
				paginator.setResult(sensors);
			} else {
				// Build the result URLs
				List<String> sensorIds = sensorDao.findSensors(sunMacAddress, username, start, end);
				ArrayList<Link> links = Lists.newArrayListWithCapacity(sensorIds.size());
				for (String sensorId : sensorIds) {
					UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path(sensorId);
					String url = builder.build().toASCIIString();
					links.add(new Link(url, null, Link.class.getSimpleName()));
				}
				paginator.setResult(links);
			}
			UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path("findBy");
			if (!StringUtils.isEmpty(sunMacAddress)) {
				builder.queryParam("sunMacAddress", sunMacAddress);
			}
			if (!StringUtils.isEmpty(username)) {
				builder.queryParam("username", username);
			}
			PaginatorUtils.addPagingLinks(paginator, builder, full);
		}
		return paginator;
	}
	
	@GET
	@Timed
	public FindByResult findSensors(@QueryParam("sunMacAddress") String sunMacAddress, @QueryParam("username") String username) {
		// Build the result URLs
		List<String> sensorIds = sensorDao.findSensors(sunMacAddress, username, 0, Integer.MAX_VALUE);
		ArrayList<String> urlLinks = Lists.newArrayListWithCapacity(sensorIds.size());
		for (String sensorId : sensorIds) {
			String url = UriBuilder.fromPath(CoreApiPath.SENSOR.getPath()).path(sensorId).build().toASCIIString();
			urlLinks.add(url);
		}
		return new FindByResult(urlLinks);
	}
	
	@POST
	@Timed
	public Sensor doPost(@Valid Sensor sensor) {
		int result = sensorDao.insert(sensor);
		if (result == 0) {
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to create sensor=" + sensor).build());
		}
		return sensorDao.get(sensor.getSensorId());
	}
	
	@Path("/{sensorId}")
	@PUT
	@Timed
	public Sensor doPut(@PathParam("sensorId") String sensorId, @Valid Sensor sensor) {
		if (!sensorId.equals(sensor.getSensorId())) {
			LOGGER.warn("Path param sensorId={} does NOT match the sensorId in the Sensor entity={}, setting entity's sensorId to the path param value", sensorId, sensor);
			sensor.setSensorId(sensorId);
		}
		int result = sensorDao.update(sensor);
		if (result == 0) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Could not update Sensor with sensorId=" + sensorId).build());
		}
		return sensorDao.get(sensor.getSensorId());
	}
	
	@Path("/{sensorId}")
	@DELETE
	@Timed
	public Response doDelete(@PathParam("sensorId") String sensorId) {
		int result = sensorDao.delete(sensorId);
		if (result == 0) {
			LOGGER.warn("DELETE operation did not affect any entities. SensorId={}", sensorId);
		}
		return Response.status(Status.OK).build();
	}
	
	private void resolveHrefLink(Sensor sensor) {
		UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path(sensor.getSensorId());
		sensor.setHref(builder.build().toASCIIString());
		return;
	}
}
