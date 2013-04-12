package com.zeedoo.api.users.dao;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.api.users.database.SqlMapper;
import com.zeedoo.api.users.database.SqlService;
import com.zeedoo.api.users.domain.User;

@Component
public class UserDao {
	
	@Timed(name = "UserDao.get")
	public User get(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid should not be null!");
		SqlSessionFactory factory = SqlService.getSessionFactory();
		SqlSession session = factory.openSession();
		SqlMapper mapper = session.getMapper(SqlMapper.class);
		try {
			return mapper.get(uuid);
		} finally {
			session.close();
		}
	}
	
	@Timed(name = "UserDao.findByUsername")
	//TODO: figure out how to time this
	public User findByUsername(String username) {
		Preconditions.checkNotNull(username, "Username should not be null!");
		SqlSessionFactory factory = SqlService.getSessionFactory();
		SqlSession session = factory.openSession();
		SqlMapper mapper = session.getMapper(SqlMapper.class);
		try {
			return mapper.findByUsername(username);
		} finally {
			session.close();
		}
	}
	
	
	public Optional<User> getByApiKey(String apiKey) {
		return Optional.absent();
	}
}
