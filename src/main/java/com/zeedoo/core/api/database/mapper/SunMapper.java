package com.zeedoo.core.api.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.Sun;

/**
 * MyBatis mapper to interact with dbo.Sun
 * @author nzhu
 *
 */
public interface SunMapper extends Mapper {
	
	List<Sun> getAllSuns(@Param("max") Integer max);
		
	List<String> findSuns(@Param("username") String username, @Param(value = "start") int start, @Param(value = "end") int end);
	
	List<Sun> findSunsFullEntity(@Param("username") String username, @Param(value = "start") int start, @Param(value = "end") int end);
	
	int findSunsCount(@Param("username") String username);
		
	Sun getSunById(@Param(value = "id") String id);
	
	Sun getSunBySunMacAddress(@Param(value = "sunMacAddress") String sunMacAddress);
	
	Sun getSunBySocketAddress(@Param(value = "ipAddress") String ipAddress, @Param(value = "port") Integer port);
	
	int insert(@Param(value = "sun") Sun sun);
	
	int update(@Param(value = "sun") Sun sun);
}
