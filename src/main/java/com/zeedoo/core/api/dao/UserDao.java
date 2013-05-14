package com.zeedoo.core.api.dao;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.core.api.database.SqlMapper;
import com.zeedoo.core.api.database.SqlService;
import com.zeedoo.core.api.domain.User;

@Component
public class UserDao {
	
	@Autowired
	SqlService sqlService;
	
	public User get(UUID uuid) {
		Preconditions.checkArgument(uuid != null);
		SqlSessionFactory factory = sqlService.getSessionFactory();
		SqlSession session = factory.openSession(true);
		SqlMapper mapper = session.getMapper(SqlMapper.class);
		try {
			return mapper.get(uuid);
		} finally {
			session.close();
		}
	}
	
	//TODO: figure out how to time this
	public User getUserByUsername(String username) {
		Preconditions.checkArgument(username != null);
		SqlSessionFactory factory = sqlService.getSessionFactory();
		SqlSession session = factory.openSession(true);
		SqlMapper mapper = session.getMapper(SqlMapper.class);
		try {
			return mapper.getUserByUsername(username);
		} finally {
			session.close();
		}
	}
	
	public User getUserByApiKey(String apiKey) {
		Preconditions.checkArgument(apiKey != null);
		SqlSessionFactory factory = sqlService.getSessionFactory();
		SqlSession session = factory.openSession(true);
		SqlMapper mapper = session.getMapper(SqlMapper.class);
		try {
			return mapper.getUserByApiKey(apiKey);
		} finally {
			session.close();
		}
	}
	
	public int updateUser(String id, User user) {
		SqlSessionFactory factory = sqlService.getSessionFactory();
		// Setting AUTOCOMMIT = true
		SqlSession session = factory.openSession(true);
		SqlMapper mapper = session.getMapper(SqlMapper.class);
		try {
			return mapper.update(id, user);
		} finally {
			session.close();
		}
	}
	
	public int insertUser(User user) {
		SqlSessionFactory factory = sqlService.getSessionFactory();
		// Setting AUTOCOMMIT = TRUE ensures the update goes through
		SqlSession session = factory.openSession(true);
		SqlMapper mapper = session.getMapper(SqlMapper.class);
		try {
			return mapper.insert(user);
		} finally {
			session.close();
		}
	}
	
	public int deleteUserByUsername(String username) {
		SqlSessionFactory factory = sqlService.getSessionFactory();
		SqlSession session = factory.openSession(true);
		SqlMapper mapper = session.getMapper(SqlMapper.class);
		try {
			return mapper.deleteUserByUsername(username);
		} finally {
			session.close();
		}
	}
	
	public SqlService getSqlService() {
		return sqlService;
	}

	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}	
}