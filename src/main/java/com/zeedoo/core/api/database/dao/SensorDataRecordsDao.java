package com.zeedoo.core.api.database.dao;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.core.api.database.mapper.SensorDataRecordsMapper;
import com.zeedoo.core.api.database.transaction.Transactional;

@Component
public class SensorDataRecordsDao extends EntityDao<SensorDataRecordsMapper> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataRecordsDao.class);
	
	// Insert a list of records
	// Note: This should be the preferred way to insert multiple records for performance reasons
	@Transactional
	public int insertDataRecords(List<SensorDataRecord> records) {
		Preconditions.checkArgument(records != null, "Records should not be null");
		SensorDataRecordsMapper mapper = getMapper();
		return mapper.insertDataRecords(records);
	}
	
	@Transactional
	public List<SensorDataRecord> get(String sensorId, DateTime start, DateTime end) {
		Preconditions.checkArgument(sensorId != null, "Sensor Id should not be null");
		SensorDataRecordsMapper mapper = getMapper();
		Long startDate = unixTimestampFromDateTime(start);
		Long endDate = unixTimestampFromDateTime(end);
		return mapper.get(sensorId, startDate, endDate);
	}
	
	@Transactional
	public int insert(SensorDataRecord record) {
		Preconditions.checkArgument(record != null, "Record should not be null");
		SensorDataRecordsMapper mapper = getMapper();
		return mapper.insert(record);
	}
	
	// For testing purposes
	@Transactional
	public int delete(String sensorId, DateTime start, DateTime end) {
    	Preconditions.checkArgument(sensorId != null, "Sensor Id should not be null");
    	SensorDataRecordsMapper mapper = getMapper();
    	Long startDate = unixTimestampFromDateTime(start);
		Long endDate = unixTimestampFromDateTime(end);
		return mapper.delete(sensorId, startDate, endDate);
	}

	@Override
	protected SensorDataRecordsMapper getMapper() {
		return databaseService.getMapper(SensorDataRecordsMapper.class);
	}
	
	public Long unixTimestampFromDateTime(DateTime dateTime) {
		if (dateTime == null) {
			return null;
		}
		// Divide by 1000L to get the unix stamp
	    return dateTime.getMillis() / 1000L;
	}

}
