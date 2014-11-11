package com.zeedoo.core.api.database.mapper;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.User;

public interface UserMapper extends Mapper{
	
	/**
	 * Users
	 */
	int findUsersCount();
	
	List<String> findUsers(@Param(value = "start") int start, @Param(value = "end") int end);
	
	List<User> findUsersFullEntity(@Param(value = "start") int start, @Param(value = "end") int end);
	
	User get(@Param(value = "uuid") UUID uuid);
	
	User getUserByUsername(@Param(value = "username") String username);
			
	int insert(@Param(value = "user") User newUser);
	
	int update(@Param(value = "id") String id, @Param(value = "user") User user);
	
	int updateLastLoginDate(@Param(value = "username") String username);
	
	int updateLastLogoutDate(@Param(value = "username") String username);
	
	int deleteUserByUsername(@Param(value = "username") String username);
}
