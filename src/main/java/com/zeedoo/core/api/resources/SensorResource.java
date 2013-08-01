package com.zeedoo.core.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

/**
 * Resource endpoint to work with Data related to Sensors
 * Core Sensor Info 
 * @author nzhu
 *
 */
@Path("/sensor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SensorResource {

}
