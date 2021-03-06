package com.zeedoo.core.api.database.dao;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.core.api.database.mapper.SqlService;
import com.zeedoo.core.api.database.mapper.UserMapper;
import com.zeedoo.core.api.database.transaction.Transactional;
import com.zeedoo.commons.domain.User;

@Component
public class UserDao extends EntityDao<UserMapper> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
		
	@Autowired
	private SqlService sqlService;
	
	@Transactional
	public int findUsersCount() {
		UserMapper mapper = getMapper();
		return mapper.findUsersCount();
	}
	
	@Transactional
	public List<String> findUsers(int start, int end) {
		UserMapper mapper = getMapper();
		return mapper.findUsers(start, end);
	}
	
	@Transactional
	public List<User> findUsersFullEntity(int start, int end) {
		UserMapper mapper = getMapper();
		return mapper.findUsersFullEntity(start, end);
	}
	
	@Transactional
	public User get(UUID uuid) {
		Preconditions.checkArgument(uuid != null, "UUID is required!");
		UserMapper mapper = getMapper();
        return mapper.get(uuid);
	}
	
	@Transactional
	public User getUserByUsername(String username) {
		Preconditions.checkArgument(username != null, "Username is required!");
		UserMapper mapper = getMapper();
        return mapper.getUserByUsername(username);
	}
	
	@Transactional
	public int updateLastLoginDate(String id) {
		Preconditions.checkArgument(id != null, "User Username / UUID is required!");
		UserMapper mapper = getMapper();
		int result = mapper.updateLastLoginDate(id);
		if (result == 0) {
			LOGGER.warn("Update Last Login Date did not affect user {}", id);
		}
		return result;
	}
	
	@Transactional
	public int updateLastLogoutDate(String id) {
		Preconditions.checkArgument(id != null, "User Username / UUID is required!");
		UserMapper mapper = getMapper();
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
		UserMapper mapper = getMapper();
		return mapper.update(id, user);
	}
	
	@Transactional
	public int insertUser(User user) {
		Preconditions.checkArgument(user != null, "User object is required!");
		UserMapper mapper = getMapper();
		return mapper.insert(user);
	}
	
	@Transactional
	public int deleteUserByUsername(String username) {
		Preconditions.checkArgument(username != null, "User Username is required!");
		UserMapper mapper = getMapper();
		return mapper.deleteUserByUsername(username);
	}
	
	public SqlService getSqlService() {
		return sqlService;
	}

	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}

	@Override
	protected UserMapper getMapper() {
		return sqlService.getMapper(UserMapper.class);
	}	
}
