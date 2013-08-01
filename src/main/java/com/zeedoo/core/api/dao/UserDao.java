package com.zeedoo.core.api.dao;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.core.api.database.SqlService;
import com.zeedoo.core.api.database.UserMapper;
import com.zeedoo.core.api.database.transaction.Transactional;
import com.zeedoo.core.domain.User;

@Component
public class UserDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
	
	@Autowired
	private SqlService sqlService;
	
	@Transactional
	public User get(UUID uuid) {
		Preconditions.checkArgument(uuid != null, "UUID is required!");
		UserMapper mapper = sqlService.getMapper(UserMapper.class);
        return mapper.get(uuid);
	}
	
	@Transactional
	public User getUserByUsername(String username) {
		Preconditions.checkArgument(username != null, "Username is required!");
		UserMapper mapper = sqlService.getMapper(UserMapper.class);
        return mapper.getUserByUsername(username);
	}
	
	@Transactional
	public int updateLastLoginDate(String id) {
		Preconditions.checkArgument(id != null, "User Username / UUID is required!");
		UserMapper mapper = sqlService.getMapper(UserMapper.class);
		int result = mapper.updateLastLoginDate(id);
		if (result == 0) {
			LOGGER.warn("Update Last Login Date did not affect user {}", id);
		}
		return result;
	}
	
	@Transactional
	public int updateLastLogoutDate(String id) {
		Preconditions.checkArgument(id != null, "User Username / UUID is required!");
		UserMapper mapper = sqlService.getMapper(UserMapper.class);
	    int result = mapper.updateLastLogoutDate(id);
		if (result == 0) {
			LOGGER.warn("Update Last Logout Date did not affect user {}", id);
		}
	    return result;
	}
		
	@Transactional
	public int updateUser(String id, User user) {
		Preconditions.checkArgument(id != null, "User Username / UUID is required!");
		Preconditions.checkArgument(user != null, "User object is required!");
		UserMapper mapper = sqlService.getMapper(UserMapper.class);
		return mapper.update(id, user);
	}
	
	@Transactional
	public int insertUser(User user) {
		Preconditions.checkArgument(user != null, "User object is required!");
		UserMapper mapper = sqlService.getMapper(UserMapper.class);
		return mapper.insert(user);
	}
	
	@Transactional
	public int deleteUserByUsername(String username) {
		Preconditions.checkArgument(username != null, "User Username is required!");
		UserMapper mapper = sqlService.getMapper(UserMapper.class);
		return mapper.deleteUserByUsername(username);
	}
	
	public SqlService getSqlService() {
		return sqlService;
	}

	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}	
}
