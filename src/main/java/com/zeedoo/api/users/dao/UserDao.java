package com.zeedoo.api.users.dao;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.zeedoo.api.users.database.SqlMapper;
import com.zeedoo.api.users.database.SqlService;
import com.zeedoo.api.users.domain.User;

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
	
	public Optional<User> getByApiKey(String apiKey) {
		return Optional.absent();
	}

	public SqlService getSqlService() {
		return sqlService;
	}

	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}	
}
