package com.zeedoo.core.api.service;

/**
 * Service layer to interact with Urban Airship API
 * @author nzhu
 *
 */
public interface UrbanAirshipService {
	
	/**
	 * Registers an Apple device token using Urban Airship API
	 * @param deviceToken
	 * @param username
	 * return boolean 
	 */
	boolean registerDeviceToken(String deviceToken, String username);
	
	/**
	 * Unregisters an Apple device token
	 * @param deviceToken
	 */
	void unregisterDeviceToken(String deviceToken);

}
