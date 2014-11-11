package com.zeedoo.core.api.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.zeedoo.commons.domain.AppData;

public class BMResourceTest extends BaseResourceTest {

	public BMResourceTest() throws Exception {
		super();
	}
	
	@Override
	public void runTests() {		
	}
	
	public void testUploadAppFile() throws IOException {
		client.removeAllFilters();
		FormDataMultiPart multiPart = new FormDataMultiPart();
		AppData appData = new AppData();
		appData.setAppId("appId");
		appData.setAppName("appName");
		appData.setAppBuildVersion("10100");
		String content = "This is a binary content";
        byte[] bytes = Files.readAllBytes(Paths.get("test.mp3"));
		multiPart.field("appData", appData, MediaType.APPLICATION_JSON_TYPE);
		multiPart.field("file", new ByteArrayInputStream(bytes), MediaType.APPLICATION_OCTET_STREAM_TYPE);
		//multiPart.field("file", new ByteArrayInputStream(content.getBytes()), MediaType.APPLICATION_OCTET_STREAM_TYPE);
		//multiPart.bodyPart(new ByteArrayInputStream(content.getBytes()), MediaType.APPLICATION_OCTET_STREAM_TYPE);
		WebResource webResource = client
				.resource("http://localhost:9898/bm/appfiles");
		webResource.type(MediaType.MULTIPART_FORM_DATA).post(String.class, multiPart);
	}
}
