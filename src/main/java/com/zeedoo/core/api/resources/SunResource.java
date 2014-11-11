package com.zeedoo.core.api.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.api.CoreApiPath;
import com.zeedoo.commons.domain.CollectionResult;
import com.zeedoo.commons.domain.Link;
import com.zeedoo.commons.domain.Paginator;
import com.zeedoo.commons.domain.Sun;
import com.zeedoo.core.api.config.SpringConfiguration;
import com.zeedoo.core.api.constants.Constants;
import com.zeedoo.core.api.database.dao.SunDao;
import com.zeedoo.core.api.utils.PaginatorUtils;

@Path("/sun")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SunResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SunResource.class);
		
	@Autowired
	private SunDao sunDao;
	
	@Autowired
	private SpringConfiguration configuration;
	
	@GET
	@Timed
	@CacheControl(maxAge = 6, maxAgeUnit = TimeUnit.HOURS)
	public CollectionResult findSuns(@QueryParam("max") Integer max) {
		List<Sun> suns = sunDao.getAllSuns(max);
		int count = suns != null ? suns.size() : 0;
		for (Sun sun : suns) {
			resolveHrefLink(sun);
		}
		return new CollectionResult(suns, count);
	}
	
	@Path("/id/{sunId}")
	@GET
	@Timed
	public Sun doGetSunBySunId(@PathParam("sunId") String sunId) {
		Optional<Sun> sun = Optional.<Sun>fromNullable(sunDao.getSunById(sunId));
		if (sun.isPresent()) {
			Sun result = sun.get();
			resolveHrefLink(result);
			return result;
		} else {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Could not find Sun with SunId: " + sunId).build());
		}
	}
	
	@Path("/macAddress/{macAddress}")
	@GET
	@Timed
	public Sun doGetSunBySunMacAddress(@PathParam("macAddress") String macAddress) {
		Optional<Sun> sun = Optional.<Sun>fromNullable(sunDao.getSunBySunMacAddress(macAddress));
		if (sun.isPresent()) {
			Sun result = sun.get();
			resolveHrefLink(result);
			return result;
		} else {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Could not find Sun with MAC Address: " + macAddress).build());
		}
	}
	
	/**
	 * If username is not null, the resource will find all Suns that manages at least one sensor that belongs to the user
	 * @param username
	 * @return
	 */
	@Path("/findBy")
	@GET
	@Timed
	public Paginator findSuns(@QueryParam("username") String username,
			@QueryParam("pageSize") Integer pageSize, @QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("fullEntity") Boolean fullEntity) {
		int totalSunsFound = sunDao.findSunsCount(username);
		Paginator paginator = PaginatorUtils.fromResultAndPagingOptions(totalSunsFound, pageSize, pageNumber);
		int actualPageSize = paginator.getPageSize();
		int actualPageNumber = paginator.getPageNumber();
		int numberOfPages = paginator.getPageCount();
		boolean full = fullEntity != null ? fullEntity : Constants.SHOW_FULL_ENTITY;
		if (numberOfPages != 0 && paginator.getResultCount() != 0) {
			int start = (actualPageNumber - 1) * actualPageSize;
			int end = actualPageSize;
			if (full) {
				List<Sun> suns = sunDao.findSunsFullEntity(username, start, end);
				// set href
				for (Sun sun : suns) {
					resolveHrefLink(sun);
				}
				paginator.setResult(suns);
			} else {
				List<String> ids = sunDao.findSuns(username, start, end);
				ArrayList<Link> links = Lists.newArrayListWithCapacity(ids.size());
				for (String id : ids) {
					UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path(CoreApiPath.SUN_ID.getPath()).path(id);
					String url = builder.build().toASCIIString();
					links.add(new Link(url, null, Link.class.getSimpleName()));
				}
				paginator.setResult(links);
			}
			UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path("findBy");
			if (!StringUtils.isEmpty(username)) {
				builder.queryParam("username", username);
			}
			PaginatorUtils.addPagingLinks(paginator, builder, full);
		}
		return paginator;
	}
	
	@Path("/socketAddress/{socketAddress}")
	@GET
	@Timed
	public Sun doGetSunBySocketAddress(@PathParam("socketAddress") String socketAddress) {
		// Validate the socket address
	    boolean validSocketAddress = true;
		if (StringUtils.isEmpty(socketAddress)) {
			validSocketAddress = false;
		}
		String[] ipPortPair = socketAddress.split(":");
		if (ipPortPair.length != 2) {
			validSocketAddress = false;
		}
		if (!validSocketAddress) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provided a valid socket address (ip:port)").build();
			throw new WebApplicationException(response);
		}
		Optional<Sun> sun = Optional.fromNullable(sunDao.getSunBySocketAddress(ipPortPair[0], Integer.parseInt(ipPortPair[1])));
		if (sun.isPresent()) {
			Sun result = sun.get();
			resolveHrefLink(result);
			return result;
		} else {
			Response response = Response.status(Status.NOT_FOUND).entity("Could not find Sun with given socketAddress=" + socketAddress).build();
			throw new WebApplicationException(response);
		}
	}
	
	@PUT
	@Timed
	public Sun doUpdateSun(@Valid Sun sun) {
		validateSunEntity(sun);
		int result = sunDao.update(sun);
		if (result == 0) {
			Response response = Response.status(Status.INTERNAL_SERVER_ERROR).entity("Could not update given Sun=" + sun).build();
			throw new WebApplicationException(response);
		}
		return sun;
	}
	
	@POST
	@Timed
	public Sun doCreateSun(@Valid Sun sun) {
		validateSunEntity(sun);
		int result = sunDao.insert(sun);
		if (result == 0) {
			Response response = Response.status(Status.INTERNAL_SERVER_ERROR).entity("Could not create given Sun=" + sun).build();
			throw new WebApplicationException(response);
		}
		return sun;
	}
	
	private void resolveHrefLink(Sun sun) {
		UriBuilder builder = UriBuilder.fromUri(configuration.getBaseUrl()).path(this.getClass()).path("id").path(sun.getId());
		sun.setHref(builder.build().toASCIIString());
		return;
	}
	
	/**
	 * Checks if any form of ID is provided
	 * @param sun
	 */
	private void validateSunEntity(Sun sun) {
		boolean validSocketAddressExists = !StringUtils.isEmpty(sun.getCurrentIpAddress()) && sun.getCurrentPort() != null;
		boolean validSun = sun != null 
				&& (!StringUtils.isEmpty(sun.getSunId())
				|| !StringUtils.isEmpty(sun.getMacAddress())
		        || validSocketAddressExists);
		if (!validSun) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provide a identifier for Sun object!").build();
			throw new WebApplicationException(response);
		}
	}

}
