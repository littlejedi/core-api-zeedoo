package com.zeedoo.core.api.cache;

import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.commons.utils.DateUtils;

public class SensorDataCacheItem {

	private DateTime startDate;
	
	private DateTime endDate;
	
	private List<SensorDataRecord> data;
	
	public SensorDataCacheItem(DateTime startDate, DateTime endDate, List<SensorDataRecord> data) {
		// start/end dates should never be null for date comparison
		this.startDate = startDate != null ? startDate : new DateTime(0L).withZone(DateTimeZone.UTC);
		this.endDate = endDate != null ? endDate : DateUtils.nowUtc();
		this.data = data;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public List<SensorDataRecord> getData() {
		return data;
	}

	public void setData(List<SensorDataRecord> data) {
		this.data = data;
	}
}
