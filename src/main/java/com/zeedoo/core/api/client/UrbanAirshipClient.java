package com.zeedoo.core.api.client;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.zeedoo.core.api.config.SpringConfiguration;

/**
 * Simple Jersey Client to talk to Urban Airship API
 * @author nzhu
 *
 */
@Component
public class UrbanAirshipClient {
	
	@Autowired
	private SpringConfiguration springConfig;
	
	private Client client;
	private WebResource resource;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UrbanAirshipClient.class);
	
	@PostConstruct
	public void init() {
		//initializes the client
		client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(springConfig.getUrbanAirshipApiKey(), springConfig.getUrbanAirshipApiSecret()));
		resource = client.resource(springConfig.getUrbanAirshipRootPath());
		LOGGER.info("Urban Airship Client Initialization Complete");
	}
	
	// Register an Apple Device Token using Urban Airship API
	// PUT /api/device_tokens/{deviceToken}
	// http://docs.urbanairship.com/reference/api/registration.html
	public boolean registerDeviceToken(String deviceToken, String userName) {
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("alias", userName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String payloadString = mapper.writeValueAsString(payload);
			ClientResponse response = resource.path(UrbanAirshipApiPath.DEVICE_TOKENS.getName())
					.path(deviceToken)
					.type(MediaType.APPLICATION_JSON)
					.put(ClientResponse.class, payloadString);
			LOGGER.info(response.toString());
			if (response.getStatus() == ClientResponse.Status.OK.getStatusCode() || response.getStatus() == ClientResponse.Status.CREATED.getStatusCode()) {
				return true;
			} else {
				LOGGER.error("Unable to register device token on Urban Airship for deviceToken = {}, userName = {}!", deviceToken, userName);
				return false;
			}
		} catch (JsonProcessingException e) {
			LOGGER.error("Error converting payload to JSON String {}", e);
		}
		return false;
	}
	
	// Unregister an Apple Device Token
	public boolean unregisterDeviceToken(String deviceToken) {
		ClientResponse response = resource.path(UrbanAirshipApiPath.DEVICE_TOKENS.getName())
				.path(deviceToken)
				.delete(ClientResponse.class);
		LOGGER.info(response.toString());
		if (response.getStatus() == ClientResponse.Status.NO_CONTENT.getStatusCode()) {
			return true;
		} else {
			LOGGER.error("Unable to un-register device token on Urban Airship for deviceToken = {}", deviceToken);
			return false;
		}
	}
	
	// Register an Android APID
	public void registerAPID(String apid, String username) {
		//TODO: Implement
	}
	
	// Send an iOS Push
    //FIXME: Implement
	public boolean sendiOSPushNotification() {
		ClientResponse response = resource.path(UrbanAirshipApiPath.PUSH.getName())
		        .type(MediaType.APPLICATION_JSON)
		        .post(ClientResponse.class, "");
		return true;
	}
	
	// Send an Android Push
}

