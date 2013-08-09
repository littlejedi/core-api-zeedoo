package com.zeedoo.core.api.resources;

import com.sun.jersey.api.client.Client;
import com.zeedoo.core.api.client.HmacClientFilter;
import com.zeedoo.core.api.database.mapper.SqlService;

public class BaseResourceTest {
	
	private static final String TEST_API_KEY = "dev";
	private static final String TEST_SECRET_KEY = "6fdd1400-a709-11e2-9e96-0800200c9a66";
	
	protected final SqlService sqlService;
	protected Client client;
	
	public BaseResourceTest() throws Exception {
		sqlService = new SqlService();
		client = Client.create();
		client.getProperties().put("api_key", TEST_API_KEY);
		client.getProperties().put("secret_key", TEST_SECRET_KEY);
		client.addFilter(new HmacClientFilter(client.getProviders()));
		System.out.println(client.getProviders());
		//DB connection
		sqlService.setEnvironment("dev");
		sqlService.init();
	}
}
