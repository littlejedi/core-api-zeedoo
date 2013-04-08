package com.zeedoo.api.users.database;


import org.apache.ibatis.annotations.Param;

import com.zeedoo.api.users.domain.User;


public interface SqlMapper {
	
	/*
	 * Users
	 */
	User selectUserByUsername(@Param(value = "username") String username);
	
}
