package com.zeedoo.core.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.StringUtils;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.domain.ApiToken;
import com.zeedoo.commons.domain.DeviceToken;
import com.zeedoo.core.api.client.UrbanAirshipClient;
import com.zeedoo.core.api.dao.DeviceTokenDao;
import com.zeedoo.core.api.hmac.Restricted;

/**
 * Resource endpoint for Apple device token related operations
 * @author nzhu
 *
 */
@Path("/deviceToken")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class DeviceTokenResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceTokenResource.class);
	
	@Autowired
	private DeviceTokenDao deviceTokenDao;

	@Autowired
	private UrbanAirshipClient uaClient;
	
	// Register an Apple Token Device, this operation is twofold
	// a) Register via Urban Airship API
	// b) Put a backup record in our own database
	@POST
	@Timed
	public Response doRegisterDeviceToken(@Restricted ApiToken apiToken, DeviceToken deviceToken) {
		if (StringUtils.isNullOrEmpty(deviceToken.getTokenId())) {
			return Response.status(Status.BAD_REQUEST).entity(String.format("Device Token is Required!")).build();
		}
		// a) UA registration
		boolean result = uaClient.registerDeviceToken(deviceToken.getTokenId(), deviceToken.getUsername());
		if (!result) {
			return Response.serverError().entity("Unable to register Device Token via Urban Airship API!").build();
		}
		// b) database backup
		deviceTokenDao.registerDeviceToken(deviceToken);
		return Response.ok("Registered Device Token!").build();
	}
	
	// Un-register an Apple token device, this operation is twofold
	// a) Unregister via Urban Airship API
	// b) Mark this device token as 'inactive' in our own database
	@Path("{tokenId}")
	@DELETE
	@Timed
	public Response doUnregisterDeviceToken(@Restricted ApiToken apiToken, @PathParam("tokenId") String tokenId) {
		// a) UA un-registration
		boolean result = uaClient.unregisterDeviceToken(tokenId);
		if (!result) {
			return Response.serverError().entity("Unable to un-register Device Token via Urban Airship API!").build();
		}
		// b) database status change
		deviceTokenDao.unregisterDeviceToken(tokenId);
		return Response.ok().build();
	}

}
