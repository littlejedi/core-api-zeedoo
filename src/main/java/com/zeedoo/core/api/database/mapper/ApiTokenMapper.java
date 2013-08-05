package com.zeedoo.core.api.database.mapper;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.ApiToken;

public interface ApiTokenMapper extends Mapper {
		
	/** 
	 * API Tokens
	 */
	ApiToken getApiToken(@Param(value = "apiKey") String apiKey);

}
