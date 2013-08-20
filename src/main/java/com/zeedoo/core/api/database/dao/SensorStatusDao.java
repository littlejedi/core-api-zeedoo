package com.zeedoo.core.api.database.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zeedoo.commons.domain.SensorStatus;
import com.zeedoo.core.api.database.mapper.SensorStatusMapper;
import com.zeedoo.core.api.database.transaction.Transactional;

@Component
public class SensorStatusDao extends EntityDao<SensorStatusMapper> {
	
	@Transactional
	public SensorStatus get(String sensorId) {
		SensorStatusMapper mapper = getMapper();
		return mapper.get(sensorId);
	}
	
	@Transactional
	public List<String> findByMacAddress(String macAddress) {
		SensorStatusMapper mapper = getMapper();
		return mapper.findByMacAddress(macAddress);
	}
	
	@Transactional
	public int update(SensorStatus sensorStatus) {
		SensorStatusMapper mapper = getMapper();
		return mapper.update(sensorStatus);
	}
	
	@Transactional
	public int insert(SensorStatus sensorStatus) {
		SensorStatusMapper mapper = getMapper();
		return mapper.insert(sensorStatus);
	}
	
	@Transactional
	public int delete(String sensorId) {
		SensorStatusMapper mapper = getMapper();
		return mapper.delete(sensorId);
	}

	@Override
	protected SensorStatusMapper getMapper() {
		return databaseService.getMapper(SensorStatusMapper.class);
	}

}
