package com.zeedoo.core.api.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.commons.domain.SensorDataRecords;
import com.zeedoo.commons.utils.DateUtils;
import com.zeedoo.core.api.database.dao.SensorDataRecordsDao;

@Component
public class SensorDataCacheService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataCacheService.class);
	
	private static Cache<String, SensorDataCacheItem> SENSOR_DATA_CACHE;
	
	@Autowired
	private SensorDataRecordsDao sensorDataRecordsDao;
	
	@PostConstruct
	public void initialize() {
		SENSOR_DATA_CACHE = CacheBuilder.newBuilder()
				.expireAfterAccess(1, TimeUnit.HOURS)
				.build();	
	}
	
	public SensorDataRecords getSensorDataFromCache(String sensorId, DateTime start, DateTime end) {
		SensorDataCacheItem cacheItem = SENSOR_DATA_CACHE.getIfPresent(sensorId);
		if (cacheItem == null || cacheItem.getData() == null || cacheItem.getData().isEmpty()) {
			return getDataFromDatabase(sensorId, start, end);
		} else {
			if (isRequestedDataInCache(start, end, cacheItem)) {
				LOGGER.info("Requested data exists in cacheItem with startDate = {} and endDate = {}", cacheItem.getStartDate(), cacheItem.getEndDate());
				return getDataFromCache(start, end, cacheItem);
			} else {
				LOGGER.info("Requested data does NOT exist in cacheItem, getting data from DB");
				return getDataFromDatabase(sensorId, start, end);
			}
		}
	}
	
	public void putSensorDataIntoCache(String sensorId, SensorDataCacheItem cacheItem) {
		SENSOR_DATA_CACHE.put(sensorId, cacheItem);
	}
	
	// Determines if we can get data from cache
	private boolean isRequestedDataInCache(DateTime startDate, DateTime endDate, SensorDataCacheItem cacheItem) {
		long requestedStartDateAsLong  = startDate != null ? startDate.getMillis() : 0L;
		long requestedEndDateAsLong = endDate != null ? endDate.getMillis() : DateUtils.nowUtc().getMillis();
		DateTime startDateInCache = cacheItem.getStartDate();
		DateTime endDateInCache = cacheItem.getEndDate();
		if (startDateInCache.getMillis() <= requestedStartDateAsLong && endDateInCache.getMillis() >= requestedEndDateAsLong) {
			return true;
		}
		return false;
	}
	
	// Get data from database
	private SensorDataRecords getDataFromDatabase(String sensorId, DateTime start, DateTime end) {
		LOGGER.debug("Getting data from DB");
		// Get data from database
		List<SensorDataRecord> records = sensorDataRecordsDao.get(sensorId, start, end);
		SensorDataRecords result = new SensorDataRecords(records);
		// Refresh cache
		putSensorDataIntoCache(sensorId, new SensorDataCacheItem(start, end, records));
		return result;
	}

	// Get data from cache
	private SensorDataRecords getDataFromCache(DateTime start, DateTime end, SensorDataCacheItem cacheItem) {
		List<SensorDataRecord> completeRecords = cacheItem.getData();
		List<SensorDataRecord> filteredRecords = Lists.newArrayList();
		for (SensorDataRecord record : completeRecords) {
			long timestamp = record.getTimestampLong();
			long requestedStartDateAsLong  = start != null ? start.getMillis() : 0L;
			long requestedEndDateAsLong = end != null ? end.getMillis() : DateUtils.nowUtc().getMillis();
			if (timestamp >= requestedStartDateAsLong && timestamp <= requestedEndDateAsLong) {
				filteredRecords.add(record);
			}
		}
		return new SensorDataRecords(filteredRecords);
	}
}
