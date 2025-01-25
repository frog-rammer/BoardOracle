package com.board.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.dto.FileDTO;
import com.board.mapper.MasterMapper;

@Service
public class MasterServiceImpl implements MasterService{

	@Autowired
	private MasterMapper mapper; 
	
	@Override
	public Map<String, Object> getMemberCount() throws Exception {
		// TODO Auto-generated method stub
		return mapper.getMemberCount();
	}

	@Override
	public List<Map<String, Object>> getMemberPostCount() throws Exception {
		// TODO Auto-generated method stub
		return mapper.getMemberPostCount();
	}

	@Override
	public List<FileDTO> getFileInfo() throws Exception {
		// TODO Auto-generated method stub
		return mapper.getFileInfo();
	}

	@Override
	public void fileDeleteWhereN() throws Exception {
		// TODO Auto-generated method stub
		mapper.fileDeleteWhereN();
	}

	@Override
	public List<FileDTO> fileSelectWhereN() throws Exception {
		// TODO Auto-generated method stub
		return mapper.fileSelectWhereN();
	}

	@Override
	public List<Map<String, Object>> getMemberReplyCount() throws Exception {
		// TODO Auto-generated method stub
		return mapper.getMemberReplyCount();
	}


}
