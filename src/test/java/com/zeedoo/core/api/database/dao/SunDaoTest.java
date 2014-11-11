package com.zeedoo.core.api.database.dao;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.zeedoo.commons.domain.DeviceStatus;
import com.zeedoo.commons.domain.Sun;

public class SunDaoTest extends EntityDaoTest {

	private SunDao sunDao;
	
	public SunDaoTest() throws Exception {
		super();
		sunDao = new SunDao();
		sunDao.setDatabaseService(databaseService);
	}
	
	@Test
	public void testGet() {
		// get by sun id
		Sun sun = sunDao.getSunById("1");
		Assert.assertEquals("1", sun.getId());
		// get by sun mac address
		sun = sunDao.getSunBySunMacAddress("00-0C-43-A5-98-67");
		Assert.assertEquals("3", sun.getId());
		// get by socket address
		sun = sunDao.getSunBySocketAddress("112.151.147.222", 12345);
		Assert.assertEquals("2", sun.getId());
	}
	
	@Test
	public void testInsert() {
		Sun sun = new Sun();
		sun.setSunId(UUID.randomUUID().toString());
		sun.setStatus(DeviceStatus.OFFLINE);
		sun.setLocation("Shanghai");
		sunDao.insert(sun);
	}
	
	@Test
	public void testUpdate() {
		Sun sun = sunDao.getSunById("1");
		String randomId = UUID.randomUUID().toString();
		sun.setSunSsid(randomId);
		sunDao.update(sun);
		sun = sunDao.getSunById("1");
		Assert.assertEquals(randomId, sun.getSunSsid());
	}
	

}
