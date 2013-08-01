package com.zeedoo.core.api.database;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.DeviceToken;

public interface DeviceTokenMapper extends Mapper{
		
	/** 
	 * Device Tokens
	 */
	int registerDeviceToken(@Param(value = "device") DeviceToken deviceToken);
	
	int unregisterDeviceToken(@Param(value = "tokenId") String tokenId);
	
	int deleteDeviceToken(@Param(value = "tokenId") String tokenId);

}
