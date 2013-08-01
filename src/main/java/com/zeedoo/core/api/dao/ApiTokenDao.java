package com.zeedoo.core.api.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.core.api.database.ApiTokenMapper;
import com.zeedoo.core.api.database.SqlService;
import com.zeedoo.core.api.database.transaction.Transactional;
import com.zeedoo.core.domain.ApiToken;

/**
 * Access API-Token related data from database
 * @author nzhu
 *
 */
@Component
public class ApiTokenDao {
	
	@Autowired
	private SqlService sqlService;
	
	@Transactional
	public ApiToken getApiToken(String apiKey) {
		Preconditions.checkArgument(apiKey != null);
		ApiTokenMapper mapper = sqlService.getMapper(ApiTokenMapper.class);
		return mapper.getApiToken(apiKey);
	}

}
