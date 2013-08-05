package com.zeedoo.core.api.database.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.core.api.database.mapper.ApiTokenMapper;
import com.zeedoo.core.api.database.mapper.SqlService;
import com.zeedoo.core.api.database.transaction.Transactional;
import com.zeedoo.commons.domain.ApiToken;

/**
 * Access API-Token related data from database
 * @author nzhu
 *
 */
@Component
public class ApiTokenDao extends EntityDao<ApiTokenMapper>{
	
	@Autowired
	private SqlService sqlService;
	
	@Transactional
	public ApiToken getApiToken(String apiKey) {
		Preconditions.checkArgument(apiKey != null);
		ApiTokenMapper mapper = getMapper();
		return mapper.getApiToken(apiKey);
	}

	@Override
	protected ApiTokenMapper getMapper() {
		return sqlService.getMapper(ApiTokenMapper.class);
	}

}
