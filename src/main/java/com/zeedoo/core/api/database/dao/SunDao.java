package com.zeedoo.core.api.database.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.Sun;
import com.zeedoo.core.api.database.mapper.SunMapper;
import com.zeedoo.core.api.database.transaction.Transactional;

/**
 * DAO to interact with dbo.Sun
 */
@Component
public class SunDao extends EntityDao<SunMapper> {
	
	private static final int DEFAULT_MAX_RESULTS = 50;
	
	@Transactional
	public List<Sun> getAllSuns(Integer max) {
		SunMapper mapper = getMapper();
		int maxResults = max != null && max <= DEFAULT_MAX_RESULTS ? max : DEFAULT_MAX_RESULTS;
		return mapper.getAllSuns(maxResults);
	}
	
	@Transactional
	public int findSunsCount(String username) {
		SunMapper mapper = getMapper();
		return mapper.findSunsCount(username);
	}
	
	@Transactional
	public List<String> findSuns(String username, int start, int end) {
		SunMapper mapper = getMapper();
        return mapper.findSuns(username, start, end);
	}
	
	@Transactional
	public List<Sun> findSunsFullEntity(String username, int start, int end) {
		SunMapper mapper = getMapper();
		return mapper.findSunsFullEntity(username, start, end);
	}
	
	@Transactional
	public Sun getSunById(String sunId) {
		Preconditions.checkArgument(sunId != null, "Sun ID should not be null");
		SunMapper mapper = getMapper();
		return mapper.getSunById(sunId);
	}
	
	@Transactional
	public Sun getSunBySunMacAddress(String sunMacAddress) {
		Preconditions.checkArgument(sunMacAddress != null, "Sun MacAddress should not be null");
		SunMapper mapper = getMapper();
		return mapper.getSunBySunMacAddress(sunMacAddress);
	}
	
	@Transactional
	public Sun getSunBySocketAddress(String ipAddress, Integer port) {
		Preconditions.checkArgument(ipAddress != null, "IP Address should not be null");
		Preconditions.checkArgument(port != null, "Port should not be null");
		SunMapper mapper = getMapper();
		return mapper.getSunBySocketAddress(ipAddress, port);
	}
	
	@Transactional
	public int insert(Sun sun) {
		Preconditions.checkArgument(sun != null, "Sun entity should not be null");
		SunMapper mapper = getMapper();
		return mapper.insert(sun);
	}
	
	@Transactional
	public int update(Sun sun) {
		Preconditions.checkArgument(sun != null, "Sun entity should not be null");
		SunMapper mapper = getMapper();
		return mapper.update(sun);
	}

	@Override
	protected SunMapper getMapper() {
		return databaseService.getMapper(SunMapper.class);
	}


}
