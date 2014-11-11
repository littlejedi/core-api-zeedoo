package com.zeedoo.core.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.mysql.jdbc.StringUtils;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.domain.FileTransferTask;
import com.zeedoo.commons.domain.FileType;
import com.zeedoo.core.api.database.dao.FileTransferTaskDao;

@Path("/fileTransferTask")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class FileTransferTaskResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileTransferTaskResource.class);
	
	@Autowired
	private FileTransferTaskDao fileTransferTaskDao;
	
	@GET
	@Timed
	public FileTransferTask doGet(@QueryParam("sunMacAddress")String sunMacAddress, @QueryParam("fileId")String fileId, @QueryParam("fileType")FileType fileType) {
		if (StringUtils.isNullOrEmpty(sunMacAddress) || StringUtils.isNullOrEmpty(fileId) || fileType == null) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provide valid sunMacAddress, fileId, and fileType").build();
			throw new WebApplicationException(response);
		}
		FileTransferTask result = fileTransferTaskDao.get(sunMacAddress, fileType, fileId);
		if (result == null) {
			Response response = Response.status(Status.NOT_FOUND).entity("Cannot find requested file transfer task").build();
			throw new WebApplicationException(response);
		}
		return result;
	}
	
	@POST
	@Timed
	public FileTransferTask doInsertOrReplace(FileTransferTask task) {
		try {
		    validateEntity(task);
		} catch (IllegalStateException exception) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provide valid task").build();
			throw new WebApplicationException(response);
		}
		int result = fileTransferTaskDao.insertOrReplace(task);
		if (result == 0) {
			LOGGER.warn("DELETE operation did not affect any rows");
		}
		return task;
	}
	
	@PUT
	@Timed
	public FileTransferTask doUpdate(FileTransferTask task) {
		try {
		    validateEntity(task);
		} catch (IllegalStateException exception) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provide valid task").build();
			throw new WebApplicationException(response);
		}
		int result = fileTransferTaskDao.update(task);
		if (result == 0) {
			LOGGER.warn("DELETE operation did not affect any rows");
		}
		return task;
	}

	@DELETE
	@Timed
	public Response doDelete(@QueryParam("sunMacAddress")String sunMacAddress, @QueryParam("fileId")String fileId, @QueryParam("fileType")FileType fileType) {
		if (StringUtils.isNullOrEmpty(sunMacAddress) || StringUtils.isNullOrEmpty(fileId) || fileType == null) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provide valid sunMacAddress, fileId, and fileTpe").build();
			throw new WebApplicationException(response);
		}
		int result = fileTransferTaskDao.delete(sunMacAddress, fileType, fileId);
		if (result == 0) {
			LOGGER.warn("DELETE operation did not affect any rows");
		}
		return Response.status(Status.NO_CONTENT).entity("DELETE successful").build();
	}
	
	private void validateEntity(FileTransferTask task) {
		Preconditions.checkState(task.getSunMacAddress() != null, "Sun MAC address should not be null");
		Preconditions.checkState(task.getFileType() != null, "File Type should not be null");
		Preconditions.checkState(task.getFileId() != null, "file ID should not be null");
		Preconditions.checkState(task.getFilePath() != null, "file path should not be null");
		Preconditions.checkState(task.getMd5() != null, "MD5 should not be null");
	}
}
