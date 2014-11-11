package com.zeedoo.core.api.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.yammer.dropwizard.jersey.params.DateTimeParam;
import com.yammer.metrics.annotation.Timed;
import com.zeedoo.commons.domain.SensorDataRecord;
import com.zeedoo.commons.domain.SensorDataRecords;
import com.zeedoo.core.api.cache.SensorDataCacheService;
import com.zeedoo.core.api.constants.Constants;
import com.zeedoo.core.api.database.dao.SensorDataRecordsDao;
import com.zeedoo.core.api.utils.FileUtils;

@Path("/sensorData")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SensorDataResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataResource.class);
	
	@Autowired
	private SensorDataCacheService sensorDataCacheService;
	
	@Autowired
	private SensorDataRecordsDao sensorDataRecordsDao;
	
	@Path("/{sensorId}")
	@GET
	@Timed
	public SensorDataRecords doGetSensorData(@PathParam("sensorId") String sensorId, 
			@QueryParam("startDate") DateTimeParam startDate, @QueryParam("endDate") DateTimeParam endDate) {
		if (sensorId == null) {
			Response response = Response.status(Status.BAD_REQUEST).entity("Must provide a valid sensor Id!").build();
			throw new WebApplicationException(response);
		}
		DateTime start = startDate != null ? startDate.get() : null;
		DateTime end = endDate != null ? endDate.get() : null;
		// Check cache first
		SensorDataRecords records = sensorDataCacheService.getSensorDataFromCache(sensorId, start, end);
		return records;
	}
	
	@Path("/export/{sensorId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@GET
	@Timed
	public Response doExportSensorData(@PathParam("sensorId") String sensorId, 
			@QueryParam("startDate") DateTimeParam startDate, @QueryParam("endDate") DateTimeParam endDate) {
		// Get data
		DateTime start = startDate != null ? startDate.get() : null;
		DateTime end = endDate != null ? endDate.get() : null;
		final SensorDataRecords records = sensorDataCacheService.getSensorDataFromCache(sensorId, start, end);
		final HSSFWorkbook workbook = createWorkbookFromData(records);		
		String directory = Constants.DEFAULT_DATA_EXPORT_DIRECTORY;
		final String fileName = getFileName(directory, sensorId, startDate, endDate);
		FileUtils.ensureParentDirectory(directory);
		StreamingOutput output = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException,
			WebApplicationException {
				workbook.write(output);				
			}
		};
		LOGGER.info("Excel file written successfully to OutputStream for sensorId={}", sensorId);
		return Response.ok(output).header("content-disposition", "attachment; filename=" + fileName).build();
	}

	@PUT
	@Timed
	public Response doInsertSensorData(@Valid SensorDataRecords records) {
		if (records == null) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Must provide a valid payload!").build());
		}
		sensorDataRecordsDao.insertDataRecords(records.getSensorDataRecords());
		return Response.ok().entity("Succesfully inserted data records").build();
	}
		
	private String getFileName(String directory, String sensorId, DateTimeParam startDate, DateTimeParam endDate) {
		DateTime start = startDate != null && startDate.get() != null? startDate.get() : new DateTime(0, DateTimeZone.UTC);
		DateTime end = endDate != null && endDate.get() != null? endDate.get() : DateTime.now(DateTimeZone.UTC);
		StringBuilder fileNameBuilder = new StringBuilder();
		fileNameBuilder.append(sensorId)
			.append(start).append("to").append(end).append(".xls");
		return fileNameBuilder.toString();
	}
	
	private HSSFWorkbook createWorkbookFromData(SensorDataRecords records) {
		final HSSFWorkbook workbook = new HSSFWorkbook();
		final List<SensorDataRecord> recordItems = records.getSensorDataRecords();
		HSSFSheet sheet = workbook.createSheet("Sensor Data sheet");
		sheet.setColumnWidth(0, 256 * 20);
		sheet.setColumnWidth(1, 256 * 30);
		sheet.setColumnWidth(2, 256 * 20);
		// Fill header rows
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("SensorId");
		headerRow.createCell(1).setCellValue("Timestamp");
		headerRow.createCell(2).setCellValue("Value");
		// Iterate through the records and fill each cell
		if (records != null && recordItems != null) {
			for (int i = 0; i < recordItems.size(); i++) {
				int rowNum = i + 1;
				// Create a new row
				Row row = sheet.createRow(rowNum);
				Cell sensorIdCell = row.createCell(0);
				sensorIdCell.setCellValue(recordItems.get(i).getSensorId());
				Cell readableTimestampCell = row.createCell(1);
				readableTimestampCell.setCellValue(recordItems.get(i).getHumanReadableTimestamp().toString());
				Cell valueCell = row.createCell(2);
				valueCell.setCellValue(recordItems.get(i).getValue());
			}
		}
		return workbook;
	}
}
