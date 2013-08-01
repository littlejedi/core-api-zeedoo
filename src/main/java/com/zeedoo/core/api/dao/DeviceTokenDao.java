package com.zeedoo.core.api.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zeedoo.core.api.database.DeviceTokenMapper;
import com.zeedoo.core.api.database.SqlService;
import com.zeedoo.core.api.database.transaction.Transactional;
import com.zeedoo.commons.domain.DeviceToken;

/**
 * Access Apple Device Token related data from database
 * @author nzhu
 *
 */
@Component
public class DeviceTokenDao extends EntityDao<DeviceTokenMapper> {
	
	@Autowired
	private SqlService sqlService;
	
	@Transactional
	public int registerDeviceToken(DeviceToken token) {
		DeviceTokenMapper mapper = getMapper();
		return mapper.registerDeviceToken(token);
	}
	
	@Transactional
	public int unregisterDeviceToken(String tokenId) {
		DeviceTokenMapper mapper = getMapper();
		return mapper.unregisterDeviceToken(tokenId);
	}
	
	@Transactional
	public int deleteDeviceToken(String tokenId) {
		DeviceTokenMapper mapper = getMapper();
		return mapper.deleteDeviceToken(tokenId);
	}
	
	@Transactional
	public SqlService getSqlService() {
		return sqlService;
	}

	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}

	@Override
	protected DeviceTokenMapper getMapper() {
		return sqlService.getMapper(DeviceTokenMapper.class);
	}
}
