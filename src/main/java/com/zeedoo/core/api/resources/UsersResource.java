package com.zeedoo.core.api.resources;

import java.util.UUID;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.core.api.dao.UserDao;
import com.zeedoo.core.api.domain.User;
import com.zeedoo.core.api.domain.UserCredentials;
import com.zeedoo.core.api.hmac.Restricted;
import com.zeedoo.core.api.utils.UuidUtils;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UsersResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UsersResource.class);

	@Autowired
	private UserDao userDao;

	/**
	 * Get a user by his/her unique identifier, either UUID or username
	 * @param id
	 * @return
	 */
	@Path("{id}")
	@GET
	@Timed
	public User doGet(@Restricted User adminUser, @PathParam("id") String id) {
		Optional<User> user = Optional.absent();
		// Check if this is UUID
		if (UuidUtils.isValidUUIDString(id)) {
			user = Optional.fromNullable(userDao.get(UUID.fromString(id)));
		} else {
			// Try to grab user by username
			user = Optional.fromNullable(userDao.getUserByUsername(id));
		}
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	/**
	 * Updates a user with the given payload
	 * Validation of the user object is not required since it's an update rather than insert
	 * @param adminUser
	 * @param id
	 * @return
	 */
	@Path("{id}")
	@PUT
	@Timed
	public User doPut(@Restricted User adminUser, @PathParam("id") String id, User user) {
        int result = userDao.updateUser(id, user);
        if (result == 0) {
        	throw new WebApplicationException(Status.NOT_FOUND);
        } else{
        	return this.doGet(adminUser, id);
        }
	}

	// Login
	@Path("/login")
	@POST
	@Timed
	public User doLogin(@Restricted User adminUser, @Valid UserCredentials credz) {
		Optional<User> user = Optional.fromNullable(userDao.getUserByUsername(credz.getUsername()));
		if (user.isPresent()) {
			BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
			// get the digest for this user
			String encryptedPassword = user.get().getPassword();
			if (passwordEncryptor.checkPassword(credz.getPassword(), encryptedPassword)) {
				// we have verified the creds are correct, return this user
				return user.get();
			} else {
				// bad login!
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	// Register
	@Path("/register")
	@POST
	@Timed
	public User doRegister(@Restricted User adminUser, @Valid User user) {
		LOGGER.info("Register User: {}", user.toString());
		// User should be already validated by annotation
		// Encrypt the password
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		String encryptedPassword = passwordEncryptor.encryptPassword(user.getPassword());
		// Store the password
		user.setPassword(encryptedPassword);
        // Generate UUID
		user.setUuid(UUID.randomUUID());
		// Use DAO to create the user
	    userDao.insertUser(user);
	    return user;
	}
}
