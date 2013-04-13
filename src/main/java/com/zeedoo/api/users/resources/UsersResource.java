package com.zeedoo.api.users.resources;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.mysql.jdbc.StringUtils;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.api.users.dao.UserDao;
import com.zeedoo.api.users.domain.User;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UsersResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UsersResource.class);
	
	@Autowired
	private UserDao userDao;
	
	@Path("{uuid}")
	@GET
	@Timed
	public User doGet(@PathParam("uuid") UUID uuid) {
		if (uuid == null || uuid.toString().isEmpty()) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		Optional<User> user = Optional.fromNullable(userDao.get(uuid));
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
		
	// Find By Queries
	@Path("findByUsername")
	@GET
	@Timed
	//TODO: Put username into an enum
	public User doFindByUsername(@QueryParam("username") String username) {
		if (StringUtils.isNullOrEmpty(username)) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		Optional<User> user = Optional.fromNullable(userDao.findByUsername(username));
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
}
