package com.board.service;

import java.util.*;

import com.board.dto.FileDTO;

public interface MasterService {
	//멤버수 가져오기 (남성,여성 ,master,user)
	public Map<String,Object> getMemberCount() throws Exception;
	//회원별 게시물 수 가져오기
	public List<Map<String, Object>> getMemberPostCount() throws Exception;
	//회원 별 댓글 수 가져오기
	public List<Map<String, Object>> getMemberReplyCount() throws Exception;
	//첨부파일 목록 가져오기
	public List<FileDTO> getFileInfo() throws Exception;
	//checkfile N 상태인 첨부파일 목록 전부 가져오기
	public List<FileDTO> fileSelectWhereN() throws Exception;
	//첨부 파일 N인 상태의 파일들 전부 삭제
		public void fileDeleteWhereN() throws Exception;
	//게시판 관리
	

}
