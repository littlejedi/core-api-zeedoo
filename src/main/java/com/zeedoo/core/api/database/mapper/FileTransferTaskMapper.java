package com.zeedoo.core.api.database.mapper;

import org.apache.ibatis.annotations.Param;

import com.zeedoo.commons.domain.FileTransferTask;
import com.zeedoo.commons.domain.FileType;

public interface FileTransferTaskMapper extends Mapper {
	
	int insertOrReplace(@Param(value = "task") FileTransferTask task);
	
	int update(@Param(value = "task") FileTransferTask task);
	
	FileTransferTask get(@Param(value = "sunMacAddress") String sunMacAddress, 
			@Param(value = "fileType") FileType fileType, 
			@Param(value = "fileId") String fileId);
	
	int delete(@Param(value = "sunMacAddress") String sunMacAddress, 
			@Param(value = "fileType") FileType fileType, 
			@Param(value = "fileId") String fileId);
}
