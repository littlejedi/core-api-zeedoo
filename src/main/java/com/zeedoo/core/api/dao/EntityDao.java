package com.zeedoo.core.api.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zeedoo.core.api.database.Mapper;
import com.zeedoo.core.api.database.SqlService;

// Basic abstract DAO class for entity
@Component
public abstract class EntityDao<M extends Mapper> {
	
	@Autowired
	protected SqlService databaseService;
	
	public void setDatabaseService(SqlService databaseService) {
		this.databaseService = databaseService;
	}

	protected abstract M getMapper();

}
