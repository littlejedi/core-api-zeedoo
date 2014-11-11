package com.zeedoo.core.api.database.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.zeedoo.commons.domain.FileTransferTask;
import com.zeedoo.commons.domain.FileType;
import com.zeedoo.core.api.database.mapper.FileTransferTaskMapper;
import com.zeedoo.core.api.database.mapper.SqlService;
import com.zeedoo.core.api.database.transaction.Transactional;

@Component
public class FileTransferTaskDao extends EntityDao<FileTransferTaskMapper> {
	
	@Autowired
	private SqlService sqlService;
	
	@Transactional
	public FileTransferTask get(String sunMacAddress, FileType fileType, String fileId) {
		Preconditions.checkState(sunMacAddress != null, "Sun MAC address should not be null");
		Preconditions.checkState(fileType != null, "File Type should not be null");
		Preconditions.checkState(fileId != null, "file ID should not be null");
		FileTransferTaskMapper mapper = getMapper();
		return mapper.get(sunMacAddress, fileType, fileId);
	}
	
	@Transactional
	public int update(FileTransferTask task) {
		Preconditions.checkState(task != null, "task should not be null");
		FileTransferTaskMapper mapper = getMapper();
		return mapper.update(task);
	}
	
	@Transactional
	public int insertOrReplace(FileTransferTask task) {
		Preconditions.checkState(task != null, "task should not be null");
		FileTransferTaskMapper mapper = getMapper();
		return mapper.insertOrReplace(task);
	}
	
	@Transactional
	public int delete(String sunMacAddress, FileType fileType, String fileId) {
		Preconditions.checkState(sunMacAddress != null, "Sun MAC address should not be null");
		Preconditions.checkState(fileType != null, "File Type should not be null");
		Preconditions.checkState(fileId != null, "file ID should not be null");
		FileTransferTaskMapper mapper = getMapper();
		return mapper.delete(sunMacAddress, fileType, fileId);
	}

	@Override
	protected FileTransferTaskMapper getMapper() {
		return databaseService.getMapper(FileTransferTaskMapper.class);
	}

}
