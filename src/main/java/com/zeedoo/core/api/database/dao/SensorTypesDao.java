package com.zeedoo.core.api.database.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zeedoo.commons.domain.SensorType;
import com.zeedoo.core.api.database.mapper.SensorTypesMapper;
import com.zeedoo.core.api.database.transaction.Transactional;

@Component
public class SensorTypesDao extends EntityDao<SensorTypesMapper> {

	@Transactional
	public List<SensorType> getSensorTypes() {
		SensorTypesMapper mapper = getMapper();
		return mapper.getSensorTypes();
	}
	
	@Override
	protected SensorTypesMapper getMapper() {
		return databaseService.getMapper(SensorTypesMapper.class);
	}

}
