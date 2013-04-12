package com.zeedoo.api.users.database;


import java.util.UUID;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.api.users.domain.User;

public interface SqlMapper {
	
	/*
	 * Users
	 */
	User get(@Param(value = "uuid") UUID uuid);
	
	User findByUsername(@Param(value = "username") String username);
	
}