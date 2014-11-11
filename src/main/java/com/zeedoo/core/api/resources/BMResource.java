package com.zeedoo.core.api.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.domain.AppData;
import com.zeedoo.core.api.constants.Constants;
import com.zeedoo.core.api.utils.FileUtils;

@Path("/bm")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class BMResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BMResource.class);
	
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/appfiles")
	@POST
	@Timed
	public String doUploadApp(
			@FormDataParam("appData") AppData appData,
			@FormDataParam("file") InputStream file,
			@FormDataParam("file") FormDataContentDisposition fileDisposition) {
		LOGGER.info("AppData={}", appData);
		String uploadedFileLocation = "";
		FileUtils.ensureParentDirectory(Constants.DEFAULT_UPLOAD_DIRECTORY);
		uploadedFileLocation = "testFile";
		//uploadedFileLocation = fileDisposition.getFileName().toLowerCase();
		writeToFile(file, uploadedFileLocation);
		return "";
	}
	
	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
		try {
			OutputStream out;
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
}
}
