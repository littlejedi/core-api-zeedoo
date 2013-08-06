package com.zeedoo.core.api.database.dao;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.SunStatus;
import com.zeedoo.core.api.database.mapper.SunStatusMapper;
import com.zeedoo.core.api.database.transaction.Transactional;

@Component
public class SunStatusDao extends EntityDao<SunStatusMapper> {
	
	@Transactional
	public SunStatus getStatusBySunId(String sunId) {
		SunStatusMapper mapper = getMapper();
		Preconditions.checkNotNull("SunId should not be null", sunId);
		return mapper.getStatusBySunId(sunId);
	}
	
	@Transactional
	public SunStatus getStatusByIpAddress(String ipAddress) {
		Preconditions.checkNotNull("IP address should not be null", ipAddress);
		SunStatusMapper mapper = getMapper();
		return mapper.getStatusByIpAddress(ipAddress);
	}
	
	@Transactional
	public int insert(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus);
		Preconditions.checkNotNull("IP address should not be null", sunStatus.getSunIpAddress());
		SunStatusMapper mapper = getMapper();
		return mapper.insert(sunStatus);
	}
	
	@Transactional
	public int update(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus);
		Preconditions.checkNotNull("IP address should not be null", sunStatus.getSunIpAddress());
		SunStatusMapper mapper = getMapper();
		return mapper.update(sunStatus);
	}
	
	@Override
	protected SunStatusMapper getMapper() {
		return databaseService.getMapper(SunStatusMapper.class);
	}

}
