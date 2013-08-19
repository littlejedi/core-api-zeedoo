package com.zeedoo.core.api.database.dao;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.SunStatus;
import com.zeedoo.core.api.database.mapper.SunStatusMapper;
import com.zeedoo.core.api.database.transaction.Transactional;

@Component
public class SunStatusDao extends EntityDao<SunStatusMapper> {
	
	@Transactional
	public SunStatus getStatusBySunMacAddress(String sunMacAddress) {
		SunStatusMapper mapper = getMapper();
		Preconditions.checkNotNull("sunMacAddress should not be null", sunMacAddress);
		return mapper.getStatusBySunMacAddress(sunMacAddress);
	}
	
	@Transactional
	public SunStatus getStatusBySocketAddress(String ipAddress, Integer port) {
		Preconditions.checkNotNull("IP address should not be null", ipAddress);
		Preconditions.checkNotNull("Port should not be null", port);
		SunStatusMapper mapper = getMapper();
		return mapper.getStatusBySocketAddress(ipAddress, port);
	}
	
	@Transactional
	public int insert(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus);
		Preconditions.checkNotNull("IP address should not be null", sunStatus.getSunIpAddress());
		Preconditions.checkNotNull("Port should not be null", sunStatus.getSunPort());
		SunStatusMapper mapper = getMapper();
		return mapper.insert(sunStatus);
	}
	
	@Transactional
	public int update(SunStatus sunStatus) {
		Preconditions.checkNotNull(sunStatus);
		Preconditions.checkNotNull("IP address should not be null", sunStatus.getSunIpAddress());
		Preconditions.checkNotNull("Port should not be null", sunStatus.getSunPort());
		SunStatusMapper mapper = getMapper();
		return mapper.update(sunStatus);
	}
	
	@Override
	protected SunStatusMapper getMapper() {
		return databaseService.getMapper(SunStatusMapper.class);
	}

}
