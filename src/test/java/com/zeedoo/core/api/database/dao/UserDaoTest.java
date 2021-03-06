package com.zeedoo.core.api.database.dao;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.zeedoo.commons.domain.User;

public class UserDaoTest extends EntityDaoTest {
	
	private static final String TEST_USER_UUID = "5102e2a9-201c-4a26-8d69-7b8b93f85a55";
	private static final String TEST_USERNAME = "littlejedi";
	
	protected final UserDao userDao;
	
	public UserDaoTest() throws Exception {
		super();
		userDao = new UserDao();
		userDao.setSqlService(databaseService);
	}

	@Test
	public void testGet() {
		User user = userDao.get(UUID.fromString(TEST_USER_UUID));
		Assert.assertEquals("littlejedi", user.getUsername());
		Assert.assertEquals("test456", user.getPassword());
	}
	
	@Test
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
		int result = userDao.insertUser(user);
		user = userDao.getUserByUsername(uuidNoDashes);
		Assert.assertNotNull(user);
		Assert.assertTrue(result == 1);
		// delete this user
		result = userDao.deleteUserByUsername(uuidNoDashes);
		Assert.assertTrue(result == 1);
	}
	
	@Test
	public void testUpdateUser() {
		User user = userDao.getUserByUsername(TEST_USERNAME);
		String oldEmail = user.getEmail();
		user.setEmail("newEmail2@blackhole");
		int result = userDao.updateUser(TEST_USERNAME, user);
		Assert.assertTrue(result == 1);
		user = userDao.getUserByUsername(TEST_USERNAME);
		Assert.assertEquals("newEmail2@blackhole", user.getEmail());
		// revert to original
		user.setEmail(oldEmail);
		userDao.updateUser(TEST_USERNAME, user);
	}

}
