package com.zeedoo.core.api.database.dao;

import com.zeedoo.core.api.database.mapper.SqlService;

public class EntityDaoTest {
	
	protected SqlService databaseService;
	
	public EntityDaoTest() throws Exception {
		databaseService = new SqlService();
		databaseService.init();
	}
}
