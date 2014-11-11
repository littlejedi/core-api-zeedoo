package com.zeedoo.core.api.database.mapper;

import java.util.List;

import com.zeedoo.commons.domain.SensorType;

public interface SensorTypesMapper extends Mapper {
	
	List<SensorType> getSensorTypes();

}
