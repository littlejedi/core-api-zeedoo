package com.zeedoo.api.users.database;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqlService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SqlService.class);
	private static final String CONFIG_PATH = "com/zeedoo/api/users/database/mybatis-config.xml";
	private static SqlSessionFactory sqlSessionFactory = null;	
	
	@Value("${config.env}")
	private String environment;
	
	@PostConstruct
	public void init() throws IOException {
		if (sqlSessionFactory == null) {
			LOGGER.info("Creating SqlSessionFactory....");
			InputStream inputStream = Resources.getResourceAsStream(CONFIG_PATH);
	        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, environment);
		}
	}
    
	public SqlSessionFactory getSessionFactory() {
	    return sqlSessionFactory;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
}
