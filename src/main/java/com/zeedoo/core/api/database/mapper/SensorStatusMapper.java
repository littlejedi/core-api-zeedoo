package com.zeedoo.core.api.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.SensorStatus;

/**
 * myBatis mapper for Sensor Status
 * @author nzhu
 *
 */
public interface  SensorStatusMapper extends Mapper {
	
	SensorStatus get(@Param(value = "sensorId") String sensorId);
	
	List<String> findByMacAddress(@Param(value = "macAddress") String macAddress);
	
	int update(@Param(value = "sensorStatus") SensorStatus sensorStatus);
	
	int insert(@Param(value = "sensorStatus") SensorStatus sensorStatus);
	
	int delete(@Param(value = "sensorId") String sensorId);
}
