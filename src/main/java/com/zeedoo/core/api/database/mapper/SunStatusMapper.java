package com.zeedoo.core.api.database.mapper;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.SunStatus;

/**
 * myBatis mapper to interact with dbo.sun_alive_state
 * @author nzhu
 *
 */
public interface SunStatusMapper extends Mapper {
	
	SunStatus getStatusBySocketAddress(@Param(value = "ipAddress") String ipAddress, @Param(value = "port") Integer port);
	
	SunStatus getStatusBySunMacAddress(@Param(value = "sunMacAddress") String sunMacAddress);
	
	int insert(@Param(value = "sunStatus") SunStatus sunStatus);
	
	int update(@Param(value = "sunStatus") SunStatus sunStatus);
}
