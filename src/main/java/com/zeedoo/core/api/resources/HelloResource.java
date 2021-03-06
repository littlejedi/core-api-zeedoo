package com.zeedoo.core.api.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.core.api.service.AppService;

@Path("/hello")
@Component
public class HelloResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloResource.class);
		
	@Autowired
    private AppService appService;

    @Value("${http.port}")
    private Integer port;

    @Value("#{dw}")
    private Configuration configuration;

    //@Value("#{dwEnv}")
    //private Environment environment;

    @GET
    @Timed
    public Response doGet(@QueryParam("username") Optional<String> username) {
        return Response.ok(String.format("%s<br/>Hello application is running on port : %d; connectorType : %s", appService.greeting(), port, configuration.getHttpConfiguration().getConnectorType())).build();
    }
    
    @POST
    @Timed
    public Response doPost(String payload) {
    	LOGGER.info(payload);
        return Response.ok(String.format("%s<br/>Hello application is running on port : %d; connectorType : %s", appService.greeting(), port, configuration.getHttpConfiguration().getConnectorType())).build();
    }

    public AppService getAppService() {
        return appService;
    }

    public Integer getPort() {
        return port;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    //public Environment getEnvironment() {
    //    return environment;
    //}
}
