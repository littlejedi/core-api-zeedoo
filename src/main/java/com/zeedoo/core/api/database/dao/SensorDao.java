package com.zeedoo.core.api.database.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zeedoo.commons.domain.Sensor;
import com.zeedoo.core.api.database.mapper.SensorMapper;
import com.zeedoo.core.api.database.transaction.Transactional;

@Component
public class SensorDao extends EntityDao<SensorMapper> {
	
	@Transactional
	public Sensor get(String sensorId) {
		SensorMapper mapper = getMapper();
		return mapper.get(sensorId);
	}
	
	@Transactional
	public int update(Sensor sensor) {
		SensorMapper mapper = getMapper();
		return mapper.update(sensor);
	}
	
	@Transactional
	public int insert(Sensor sensor) {
		SensorMapper mapper = getMapper();
		return mapper.insert(sensor);
	}
	
	@Transactional
	public int delete(String sensorId) {
		SensorMapper mapper = getMapper();
		return mapper.delete(sensorId);
	}
	
	@Transactional
	public List<String> findSensors(String sunMacAddress, String username, int start, int end) {
		SensorMapper mapper = getMapper();
		return mapper.findSensors(sunMacAddress, username, start, end);
	}
	
	@Transactional
	public List<Sensor> findSensorsFullEntity(String sunMacAddress, String username, int start, int end) {
		SensorMapper mapper = getMapper();
		return mapper.findSensorsFullEntity(sunMacAddress, username, start, end);
	}
	
	@Transactional
	public int findSensorsCount(String sunMacAddress, String username) {
		SensorMapper mapper = getMapper();
		return mapper.findSensorsCount(sunMacAddress, username);
	}
	
	@Override
	protected SensorMapper getMapper() {
		return databaseService.getMapper(SensorMapper.class);
	}
}
