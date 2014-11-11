package com.zeedoo.core.api.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.Sensor;

public interface SensorMapper extends Mapper {
	
	Sensor get(@Param(value = "sensorId") String sensorId);
	
	List<String> findSensors(@Param(value = "sunMacAddress") String sunMacAddress, @Param(value = "username") String username,
			@Param(value = "start") int start, @Param(value = "end") int end);
	
	List<Sensor> findSensorsFullEntity(@Param(value = "sunMacAddress") String sunMacAddress, @Param(value = "username") String username,
			@Param(value = "start") int start, @Param(value = "end") int end);
	
	int findSensorsCount(@Param(value = "sunMacAddress") String sunMacAddress, @Param(value = "username") String username);
		
	int update(@Param(value = "sensor") Sensor sensor);
	
	int insert(@Param(value = "sensor") Sensor sensor);
	
	int delete(@Param(value = "sensorId") String sensorId);

}
