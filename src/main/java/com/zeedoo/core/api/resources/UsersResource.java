package com.zeedoo.core.api.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.ApiToken;
import com.zeedoo.commons.domain.CollectionResult;
import com.zeedoo.commons.domain.Link;
import com.zeedoo.commons.domain.Paginator;
import com.zeedoo.commons.domain.Sun;
import com.zeedoo.commons.domain.User;
import com.zeedoo.commons.domain.UserCredentials;
import com.zeedoo.core.api.config.SpringConfiguration;
import com.zeedoo.core.api.constants.Constants;
import com.zeedoo.core.api.database.dao.UserDao;
import com.zeedoo.core.api.hmac.Restricted;
import com.zeedoo.core.api.utils.PaginatorUtils;
import com.zeedoo.core.api.utils.UuidUtils;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UsersResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(UsersResource.class);

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SpringConfiguration configuration;
	
	@Path("findBy")
	@GET
	@Timed
	public Paginator findUsers(@Restricted ApiToken apiToken, @QueryParam("pageSize") Integer pageSize, @QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("fullEntity") Boolean fullEntity) {
		int totalUsersFound = userDao.findUsersCount();
		Paginator paginator = PaginatorUtils.fromResultAndPagingOptions(totalUsersFound, pageSize, pageNumber);
		int actualPageSize = paginator.getPageSize();
		int actualPageNumber = paginator.getPageNumber();
		int numberOfPages = paginator.getPageCount();
		boolean full = fullEntity != null ? fullEntity : Constants.SHOW_FULL_ENTITY;
		if (numberOfPages != 0 && paginator.getResultCount() != 0) {
			int start = (actualPageNumber - 1) * actualPageSize;
			int end = actualPageSize;
			if (full) {
				List<User> users = userDao.findUsersFullEntity(start, end);
				// set href
				for (User user : users) {
					resolveHrefLink(user);
				}
				paginator.setResult(users);
			} else {
				List<String> ids = userDao.findUsers(start, end);
				ArrayList<Link> links = Lists.newArrayListWithCapacity(ids.size());
				for (String id : ids) {
					UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path(id);
					String url = builder.build().toASCIIString();
					links.add(new Link(url, null, Link.class.getSimpleName()));
				}
				paginator.setResult(links);
			}
			UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path("findBy");
			PaginatorUtils.addPagingLinks(paginator, builder, full);
		}
		return paginator;
	}

	/**
	 * Get a user by his/her unique identifier, either UUID or username
	 * @param id
	 * @return
	 */
	@Path("{id}")
	@GET
	@Timed
	public User doGet(@Restricted ApiToken apiToken, @PathParam("id") String id) {
		Optional<User> user = Optional.absent();
		user = Optional.fromNullable(userDao.getUserByUsername(id));
		if (user.isPresent()) {
			User result = user.get();
			resolveHrefLink(result);
			return result;
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
	public User doPut(@Restricted ApiToken apiToken, @PathParam("id") String id, User user) {
        int result = userDao.updateUser(id, user);
        if (result == 0) {
        	throw new WebApplicationException(Status.NOT_FOUND);
        } else{
        	return this.doGet(apiToken, id);
        }
	}

	// Login
	@Path("/login")
	@POST
	@Timed
	public User doLogin(@Restricted ApiToken apiToken, @Valid UserCredentials credz) {
		Optional<User> user = Optional.fromNullable(userDao.getUserByUsername(credz.getUsername()));
		if (user.isPresent()) {
			BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
			// get the digest for this user
			String encryptedPassword = user.get().getPassword();
			if (passwordEncryptor.checkPassword(credz.getPassword(), encryptedPassword)) {
				// we have verified the credz are correct, update the timestamp and return this user
				userDao.updateLastLoginDate(credz.getUsername());
				return user.get();
			} else {
				// bad login!
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	// Log out
	@Path("/logout/{id}")
	@POST
	@Timed
	public Response doLogout(@Restricted ApiToken apiToken, @PathParam("id") String id) {
		Optional<User> user = Optional.fromNullable(userDao.getUserByUsername(id));
		if (user.isPresent()) {
			userDao.updateLastLogoutDate(id);
			return Response.ok(String.format("User %s Log Out successful", id)).build();
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	// Register
	@Path("/register")
	@POST
	@Timed
	public User doRegister(@Restricted ApiToken apiToken, @Valid User user) {
		LOGGER.info("Registering User: {}", user.toString());
		// User should be already validated by annotation
		// Encrypt the password
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		String encryptedPassword = passwordEncryptor.encryptPassword(user.getPassword());
		// Store the password
		user.setPassword(encryptedPassword);
		// Use DAO to create the user
	    userDao.insertUser(user);
	    return user;
	}
	
	private void resolveHrefLink(User user) {
		UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path(user.getUsername());
		user.setHref(builder.build().toASCIIString());
		return;
	}
}
