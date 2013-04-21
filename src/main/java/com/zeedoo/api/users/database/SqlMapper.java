package com.zeedoo.api.users.database;


import java.util.UUID;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.api.users.domain.User;

public interface SqlMapper {
	
	/*
	 * Users
	 */
	User get(@Param(value = "uuid") UUID uuid);
	
	User getUserByUsername(@Param(value = "username") String username);
		
	User getUserByApiKey(@Param(value = "apiKey") String apiKey);
	
	int insert(@Param(value = "user") User newUser);
	
	int update(@Param(value = "id") String id, @Param(value = "user") User user);
	
	int deleteUserByUsername(@Param(value = "username") String username);
}
