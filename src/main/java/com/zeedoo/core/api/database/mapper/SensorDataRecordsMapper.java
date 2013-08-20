package com.zeedoo.core.api.database.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.SensorDataRecord;

public interface SensorDataRecordsMapper extends Mapper {
	
	List<SensorDataRecord> get(@Param(value = "sensorId") String sensorId, 
			@Param(value = "start") Long start, @Param(value = "end") Long end);
	
	// Insert(update) the sensor data record with the given payload
	int insert(@Param(value = "record") SensorDataRecord record);
	
	// Insert multiple records
	int insertDataRecords(@Param(value = "records") List<SensorDataRecord> records);
	
	// Delete data in the record
	int delete(@Param(value = "sensorId") String sensorId, 
			@Param(value = "start") Long start, @Param(value = "end") Long end);	
}
