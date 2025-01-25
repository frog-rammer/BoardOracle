package com.board.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;

import com.board.dto.BoardDTO;
import com.board.dto.FileDTO;
import com.board.service.BoardService;
import com.board.service.MasterService;
import com.board.util.Page;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor 
public class MasterController {
	

	private final MasterService service;
	private final BoardService boardService;
	private final DataSource dataSource;
    
	@GetMapping("/master/sysManage")
	public void getSysManage() {
		
	}
	
	@GetMapping("/master/sysBoardManage")
	public void getSysBoardManage(Model model,@RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword) throws Exception {

		int postNum = 5; //한 화면에 보여지는 게시물 행의 갯수
		int startPoint = (pageNum-1) * postNum + 1; //페이지 시작 게시물 번호
		int endPoint = pageNum * postNum;
		int pageListCount = 5; //화면 하단에 보여지는 페이지리스트의 페이지 갯수		
		int totalCount = boardService.getTotalCount(keyword); //전체 게시물 갯수	
		
		Page page = new Page();

		model.addAttribute("list", boardService.list(startPoint,endPoint,keyword));
		model.addAttribute("totalElement", boardService.getTotalCount(keyword));
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getPageListMaster(pageNum, postNum, pageListCount,totalCount,keyword));
	}
	//게시물 삭제하기
	@Transactional
	@GetMapping("/master/delete")
	public String getDelete(@RequestParam("seqno") int seqno) throws Exception {
		//transaction 시작
		boardService.fileInfoUpdate(seqno); //tbl_file 테이블의 checkfile column 값을 'N'로 변환
		boardService.delete(seqno); //게시물 행 삭제
		//transaction의 끝
		return "redirect:/master/sysBoardManage?page=1";
	}
	
	@GetMapping("/master/sysFileManage")
	public void getSysFileManage(Model model) throws Exception {
		List<FileDTO> files = service.getFileInfo();
		model.addAttribute("files", files);
	}
	
	@PostMapping("/master/deleteFiles")
	@ResponseBody
	public String deleteFiles() throws Exception {
	    // 파일 삭제
		String path = "c:\\Repository\\file\\";
		
		List<FileDTO> files = service.fileSelectWhereN();
		  for (FileDTO file : files) {
		        // 파일의 전체 경로를 구성
		        String filePath = path + file.getStored_filename();
		        Path fileToDelete = Paths.get(filePath);
		        try {
		            // 파일 시스템에서 파일을 삭제.
		            Files.delete(fileToDelete);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		service.fileDeleteWhereN();
	    return "{\"message\":\"GOOD\"}";
	}
	

	@GetMapping("/master/sysMemberCount")
	public void getSysMemberCount(Model model) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.putAll(service.getMemberCount());
		
	    model.addAttribute("TOTAL_COUNT", map.get("TOTAL_COUNT"));
	    model.addAttribute("MALE_COUNT", map.get("MALE_COUNT"));
	    model.addAttribute("FEMALE_COUNT", map.get("FEMALE_COUNT"));
	    model.addAttribute("MASTER_COUNT", map.get("MASTER_COUNT"));
	    model.addAttribute("USER_COUNT", map.get("USER_COUNT"));		
	}
	

	@GetMapping("/master/sysMemberPostCount")
	public void getSysMemberPostCount(Model model) throws Exception{
		 List<Map<String, Object>> memberPostCounts = service.getMemberPostCount();
		 model.addAttribute("memberPostCounts", memberPostCounts);
	}
	

	@GetMapping("/master/sysMemberReplyCount")
	public void getSysMemberReplyCount(Model model) throws Exception {
		List<Map<String, Object>> memberReplyCount = service.getMemberReplyCount();
		 model.addAttribute("memberReplyCounts", memberReplyCount);
	}
	

	@GetMapping("/master/sysSystemInfo")
	public void getSysSystemInfo(Model model) {
	     Map<String, Object> systemInfo = new HashMap<>();
	        // 운영체제 정보 수집
	        systemInfo.put("osName", System.getProperty("os.name"));
	        systemInfo.put("osVersion", System.getProperty("os.version"));
	        // Java 버전 정보 수집
	        systemInfo.put("javaVersion", System.getProperty("java.version"));
	        // Oracle 데이터베이스 정보 수집
	        try (Connection connection = dataSource.getConnection()) {
	            DatabaseMetaData metaData = connection.getMetaData();
	            systemInfo.put("databaseProductName", metaData.getDatabaseProductName());
	            systemInfo.put("databaseProductVersion", metaData.getDatabaseProductVersion());
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        // 타임리프 버전 정보
	        systemInfo.put("thymeleafVersion", TemplateEngine.class.getPackage().getImplementationVersion());
	      model.addAttribute("systemInfo", systemInfo);
	}
	


}
