package com.zeedoo.core.api.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.mysql.jdbc.StringUtils;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.domain.SunStatus;
import com.zeedoo.core.api.database.dao.SunStatusDao;

@Path("/sunStatus")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SunStatusResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SunStatusResource.class);
	
	@Autowired
	private SunStatusDao sunStatusDao;
	
	@Path("/{ipAddress}")
	@GET
	@Timed
	public SunStatus doGetByIpAddress(@PathParam("ipAddress") String ipAddress) {
		if (StringUtils.isEmptyOrWhitespaceOnly(ipAddress)) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provided a valid ip address for findBy").build();
			throw new WebApplicationException(response);
		}
		Optional<SunStatus> sunStatus = Optional.fromNullable(sunStatusDao.getStatusByIpAddress(ipAddress));
		if (sunStatus.isPresent()) {
			return sunStatus.get();
		} else {
			Response response = Response.status(Status.NOT_FOUND).entity("Could not find a SunStatus with given ipAddress=" + ipAddress).build();
			throw new WebApplicationException(response);
		}
	}
	
	@POST
	@Timed
	public SunStatus doCreateSunStatus(@Valid SunStatus sunStatus) {
		if (sunStatus == null) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provided a valid SunStatus entity").build();
			throw new WebApplicationException(response);
		}
		sunStatusDao.insert(sunStatus);
		return sunStatusDao.getStatusByIpAddress(sunStatus.getSunIpAddress());
	}
	
	@Path("/{ipAddress}")
	@PUT
	@Timed
	public SunStatus doUpdateSunStatus(@PathParam("ipAddress") String ipAddress, @Valid SunStatus sunStatus) {
		if (sunStatus == null) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provided a valid SunStatus entity").build();
			throw new WebApplicationException(response);
		}		
		if (!ipAddress.equals(sunStatus.getSunIpAddress())) {
			LOGGER.warn("Provided path param ip address does not equal to the ip address in the entity, setting the entity's ip address to:" + ipAddress);
			sunStatus.setSunIpAddress(ipAddress);
		}
		sunStatusDao.update(sunStatus);
		return sunStatusDao.getStatusByIpAddress(sunStatus.getSunIpAddress());
	}
	
	@Path("/findBySunId")
	@GET
	@Timed
	public SunStatus doFindBySunId(@QueryParam("sunId") String sunId) {
		if (StringUtils.isEmptyOrWhitespaceOnly(sunId)) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provided a valid Sun Id for findBy").build();
			throw new WebApplicationException(response);
		}
		Optional<SunStatus> sunStatus = Optional.fromNullable(sunStatusDao.getStatusBySunId(sunId));
		if (sunStatus.isPresent()) {
			return sunStatus.get();
		} else {
			Response response = Response.status(Status.NOT_FOUND).entity("Could not find a SunStatus with given sunId=" + sunId).build();
			throw new WebApplicationException(response);
		}
	}

}
