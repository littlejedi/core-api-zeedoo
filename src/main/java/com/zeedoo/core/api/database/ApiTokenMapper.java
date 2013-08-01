package com.zeedoo.core.api.database;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.core.domain.ApiToken;

public interface ApiTokenMapper {
		
	/** 
	 * API Tokens
	 */
	ApiToken getApiToken(@Param(value = "apiKey") String apiKey);

}
