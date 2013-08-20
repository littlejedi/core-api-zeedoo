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
	
	@Path("/{socketAddress}")
	@GET
	@Timed
	public SunStatus doGetBySocketAddress(@PathParam("socketAddress") String socketAddress) {
		// Validate the socket address
		boolean validSocketAddress = true;
		if (StringUtils.isEmptyOrWhitespaceOnly(socketAddress)) {
			validSocketAddress = false;
		}
		String[] ipPortPair = socketAddress.split(":");
		if (ipPortPair.length != 2) {
			validSocketAddress = false;
		}
		if (!validSocketAddress) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provided a valid socket address (ip:port)").build();
			throw new WebApplicationException(response);
		}
		Optional<SunStatus> sunStatus = Optional.fromNullable(sunStatusDao.getStatusBySocketAddress(ipPortPair[0], Integer.parseInt(ipPortPair[1])));
		if (sunStatus.isPresent()) {
			return sunStatus.get();
		} else {
			Response response = Response.status(Status.NOT_FOUND).entity("Could not find a SunStatus with given socketAddress=" + socketAddress).build();
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
		return sunStatusDao.getStatusBySocketAddress(sunStatus.getSunIpAddress(), sunStatus.getSunPort());
	}
	
	@Path("/{socketAddress}")
	@PUT
	@Timed
	public SunStatus doUpdateSunStatus(@PathParam("socketAddress") String socketAddress, @Valid SunStatus sunStatus) {
		if (sunStatus == null) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provided a valid SunStatus entity").build();
			throw new WebApplicationException(response);
		}
		final String[] ipPortPair = socketAddress.split(":");
		if (ipPortPair.length != 2) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provided a valid socket address (ip:port)").build();
			throw new WebApplicationException(response);
		}
		final String ipAddress = ipPortPair[0];
		final String port = ipPortPair[1];
		if (!ipAddress.equals(sunStatus.getSunIpAddress())) {
			LOGGER.warn("Provided path param ip address does not equal to the ip address in the entity, setting the entity's ip address to:" + ipAddress);
			sunStatus.setSunIpAddress(ipAddress);
		}
		if (!port.equals(String.valueOf(sunStatus.getSunPort()))) {
			LOGGER.warn("Provided path param port does not equal to the port in the entity, setting the entity's port to:" + port);
			sunStatus.setSunPort(Integer.valueOf(port));
		}
		sunStatusDao.update(sunStatus);
		return sunStatusDao.getStatusBySocketAddress(sunStatus.getSunIpAddress(), sunStatus.getSunPort());
	}
	
	@Path("/findByMacAddress")
	@GET
	@Timed
	public SunStatus doFindBySunMacAddress(@QueryParam("sunMacAddress") String sunMacAddress) {
		if (StringUtils.isEmptyOrWhitespaceOnly(sunMacAddress)) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provided a valid Sun MAC Address for findBy").build();
			throw new WebApplicationException(response);
		}
		Optional<SunStatus> sunStatus = Optional.fromNullable(sunStatusDao.getStatusBySunMacAddress(sunMacAddress));
		if (sunStatus.isPresent()) {
			return sunStatus.get();
		} else {
			Response response = Response.status(Status.NOT_FOUND).entity("Could not find a SunStatus with given sunMacAddress=" + sunMacAddress).build();
			throw new WebApplicationException(response);
		}
	}

}
