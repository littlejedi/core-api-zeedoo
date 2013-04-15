package com.zeedoo.api.users.database;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.zeedoo.api.users.dao.UserDao;
import com.zeedoo.api.users.domain.User;

//TODO: Use this as BaseDaoTest
public class UserDaoTest {
	
	private static final String TEST_USER_UUID = "5102e2a9-201c-4a26-8d69-7b8b93f85a55";
	private static final String TEST_USERNAME = "littlejedi";
	
	protected SqlService sqlService;
	private UserDao userDao;

	@Before
	public void setUp() throws Exception {
		sqlService = new SqlService();
		sqlService.setEnvironment("dev");
		sqlService.init();
		userDao = new UserDao();
		userDao.setSqlService(sqlService);
	}

	@Test
	@Ignore
	public void testGet() {
		User user = userDao.get(UUID.fromString(TEST_USER_UUID));
		Assert.assertEquals("littlejedi", user.getUsername());
		Assert.assertEquals("test456", user.getPassword());
	}
	
	@Test
	@Ignore
	public void testGetUserByUsername() {
		User user = userDao.getUserByUsername(TEST_USERNAME);
		Assert.assertEquals("littlejedi", user.getUsername());
		Assert.assertEquals("test456", user.getPassword());
	}
	
	@Test
	public void testInsertUser() {
		User user = new User();
		UUID uuid = UUID.randomUUID();
		String uuidNoDashes = uuid.toString().replace("-", "");
		user.setUsername(uuidNoDashes);
		user.setPassword("password");
		user.setUuid(uuid);
		int result = userDao.insertUser(user);
		user = userDao.getUserByUsername(uuidNoDashes);
		Assert.assertNotNull(user);
		Assert.assertTrue(result == 1);
		// delete this user
		result = userDao.deleteUserByUsername(uuidNoDashes);
		Assert.assertTrue(result == 1);
	}

}
