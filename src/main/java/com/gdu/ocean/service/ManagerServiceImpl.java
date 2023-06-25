package com.gdu.ocean.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.ocean.domain.CdDTO;
import com.gdu.ocean.domain.HashtagCdDTO;
import com.gdu.ocean.domain.HashtagDTO;
import com.gdu.ocean.domain.OrderDTO;
import com.gdu.ocean.domain.OutUsersDTO;
import com.gdu.ocean.domain.ReplyDTO;
import com.gdu.ocean.domain.SleepUsersDTO;
import com.gdu.ocean.domain.UsersDTO;
import com.gdu.ocean.mapper.ManagerMapper;
import com.gdu.ocean.util.MyFileUtil;
import com.gdu.ocean.util.PageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

	private final ManagerMapper managerMapper;
	private final PageUtil pageUtil;
	private final MyFileUtil myFileUtil;
	
	@Override
	public List<HashtagDTO> getHashtagList() {
		return managerMapper.getHashtagList();
	}
	
	@Override
	public void getSaleList(HttpServletRequest request, Model model) {
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("query"));
		String query = opt1.orElse("");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("query", query);
		
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt2.orElse("1"));
		
		int totalRecord = managerMapper.getCdCount();
		
		int recordPerPage = 10;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		
		
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<CdDTO> cdList = managerMapper.getSaleList(map);
		model.addAttribute("cdList", cdList);
		model.addAttribute("pagination", pageUtil.getPagination("/manager/salelist.do?query=" + query));

		
	}
	
	@Override
	public Map<String, Object> getHashtagByNo(String cdNos) {
		Map<String, Object> map = new HashMap<>();
		String[] cdNo = cdNos.split(",");
		List<HashtagDTO> list = managerMapper.getHashtagByNo(cdNo);
		map.put("hashtagList", list);
		return map;
	}
	
	@Override
	public int removeCd(int cdNo) {
		int removeResult = managerMapper.removeCd(cdNo);
		return removeResult;
	}
	
	@Transactional
	   @Override
	   public int addCd(MultipartHttpServletRequest multipartRequest) throws Exception {
	      
	      String title = multipartRequest.getParameter("title");
	      String singer = multipartRequest.getParameter("singer");
	      int price = Integer.parseInt(multipartRequest.getParameter("price"));
	      int stock = Integer.parseInt(multipartRequest.getParameter("stock"));
	      
	      CdDTO cdDTO = new CdDTO();
	      cdDTO.setTitle(title);
	      cdDTO.setSinger(singer);
	      cdDTO.setPrice(price);
	      cdDTO.setStock(stock);

	      MultipartFile mainImgFile = multipartRequest.getFile("mainImg");
	      if(mainImgFile != null && mainImgFile.isEmpty() == false) {
	         
	         // 첨부파일 HDD에 저장하는 코드
	         String path = myFileUtil.getCdImgPath();                                        // 첨부 파일의 저장 경로
	         String mainImgFilename = myFileUtil.getFilesystemName(mainImgFile.getOriginalFilename());   // 첨부 파일의 원래 이름 가져오기
	         File dir = new File(path);
	         if(dir.exists() == false) {
	            dir.mkdirs();
	         }
	         File file = new File(dir, mainImgFilename);
	         mainImgFile.transferTo(file);   // 실제 서버에 저장
	         cdDTO.setMainImg(path + mainImgFilename);         
	      }
	      
	      MultipartFile detailImgFile = multipartRequest.getFile("detailImg");
	      if(detailImgFile != null && detailImgFile.isEmpty() == false) {
	         // 첨부파일 HDD에 저장하는 코드
	         String path = myFileUtil.getCdImgPath();
	         String detailImgFilename = myFileUtil.getFilesystemName(detailImgFile.getOriginalFilename());
	         File dir = new File(path);
	         if(dir.exists() == false) {
	            dir.mkdirs();
	         }
	         File file = new File(dir, detailImgFilename);
	         detailImgFile.transferTo(file);   // 실제 서버에 저장
	         cdDTO.setDetailImg(path + detailImgFilename);         
	      }
	      
	      int addCdResult = managerMapper.addCd(cdDTO);
	      
	      // hashtag_cd 테이블
	      // pk ht_no cd_no
	      
	      // 삽입한 CD의 CD_NO
	      int cdNo = cdDTO.getCdNo();
	      String[] hashtagNoList = multipartRequest.getParameterValues("hashtagNoList");    // cdadd.html에서 #체크한 밸류 넘어옴(3개 체크 할 경우 3개가넘어옴)
	      for(int i = 0; i < hashtagNoList.length; i++) {
	         CdDTO cdDTO2 = new CdDTO();
	         cdDTO2.setCdNo(cdNo);
	         
	         // CdDTO cdDTO3 = managerMapper.getCdInfoByCdNo(cdNo);
	         HashtagDTO hashtagDTO = new HashtagDTO();
	         hashtagDTO.setHtNo(Integer.parseInt(hashtagNoList[i]));
	         
	         
	         HashtagCdDTO hashtagCdDTO = new HashtagCdDTO();
	         hashtagCdDTO.setCdDTO(cdDTO2);   // hashtagCdDTO.setCdDTO(cdDTO2);
	         // System.out.println(hashtagCdDTO.getCdNo().getPrice());
	         hashtagCdDTO.setHashtagDTO(hashtagDTO);
	         managerMapper.addHashtagCd(hashtagCdDTO);
	      }
	      
	      
	      return addCdResult;
	      
	   }
	
	@Override
	public void getUserList(HttpServletRequest request, Model model) {
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("column"));
		String column = opt1.orElse("");
		
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("query"));
		String query = opt2.orElse("");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("column", column);
		map.put("query", query);
		
		Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt3.orElse("1"));
		
		int totalRecord = managerMapper.getUserSearchCount(map);
		
		int recordPerPage = 10;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<UsersDTO> userList = managerMapper.getUserList(map);
		
		model.addAttribute("userList", userList);
		model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/manager/membersearch.do?column=" + column + "&query=" + query));
		model.addAttribute("userNo", totalRecord - (page - 1) * recordPerPage);
		
		
	}
	
	@Override
	public void getSleepUserList(HttpServletRequest request, Model model) {
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("column"));
		String column = opt1.orElse("");
		
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("query"));
		String query = opt2.orElse("");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("column", column);
		map.put("query", query);
		
		Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt3.orElse("1"));
		
		int totalRecord = managerMapper.getSleepUserSearchCount(map);
		
		int recordPerPage = 10;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<SleepUsersDTO> sleepUserList = managerMapper.getSleepUserList(map);
		
		model.addAttribute("sleepUserList", sleepUserList);
		model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/manager/sleepmember.do?column=" + column + "&query=" + query));
		model.addAttribute("sleepUserNo", totalRecord - (page - 1) * recordPerPage);
		
		System.out.println("sleepUserList" + sleepUserList);
	}
	@Override
	public void getOutUserList(HttpServletRequest request, Model model) {
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("column"));
		String column = opt1.orElse("");
		
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("query"));
		String query = opt2.orElse("");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("column", column);
		map.put("query", query);
		
		Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt3.orElse("1"));
		
		int totalRecord = managerMapper.getOutUserSearchCount(map);
		
		int recordPerPage = 10;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<OutUsersDTO> outUserList = managerMapper.getOutUserList(map);
		
		model.addAttribute("outUserList", outUserList);
		model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/manager/outmember.do?query=" + query));
		model.addAttribute("outUserNo", totalRecord - (page - 1) * recordPerPage);
		
	}
	
	@Transactional(readOnly=false)
	@Override
	public int userout(String email, HttpServletRequest request, HttpServletResponse response) {
		log.info(email + "..................탈퇴시킬 email");
		UsersDTO usersDTO = managerMapper.selectUserById(email);
		
		int insertResult = managerMapper.insertOutUser(usersDTO);
		int deleteResult = managerMapper.deleteUser(email);
		return deleteResult;
		/*
		try {
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			if(insertResult == 1 && deleteResult == 1) {
								
				out.println("alert('탈퇴 회원으로 이동되었습니다.');");
				out.println("location.href='/manager/membersearch.do';");
				
			} else {
				out.println("alert('정보를 확인해주세요.');");
				out.println("history.back();");
			}
			out.println("</script>");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
	}
	
	/* 게시판 목록 및 검색, 페이지네이션 */
	@Override
	public void getBoardList(HttpServletRequest request, Model model) {
		
		Optional<String> opt = Optional.ofNullable(request.getParameter("column"));
		String column = opt.orElse("");
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("query"));
		String query = opt1.orElse("");
		
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt2.orElse("1"));
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("column", column);
		map.put("query", query);
		
		int totalRecord = managerMapper.getBoardCount();
		
		int recordPerPage = 10;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<ReplyDTO> replyList = managerMapper.getBoardList(map);
		model.addAttribute("replyList", replyList);
		model.addAttribute("pagination", pageUtil.getPagination("/manager/board.do?column=" + column + "&query=" + query));
		model.addAttribute("idolNo", totalRecord - (page - 1) * recordPerPage);
	}
	
	@Override
	public int removeReply(int replyNo) {
		int removeResult = managerMapper.removeReply(replyNo);
		return removeResult;
		
	}
	
   	@Override
	   public ResponseEntity<byte[]> display(int cdNo) {
	      CdDTO cdDTO = managerMapper.getCdByNo(cdNo);
	      ResponseEntity<byte[]> image = null;
	      try {
	         File mainImg = new File(cdDTO.getMainImg());
	         if (mainImg.exists()) {
	               FileInputStream inputStream = new FileInputStream(mainImg);
	               byte[] imageBytes = IOUtils.toByteArray(inputStream);
	               image = new ResponseEntity<>(imageBytes, HttpStatus.OK);
	         } 
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	      return image;
	   }
	   
	   @Override
	   public ResponseEntity<byte[]> displaydetail(int cdNo) {
		   CdDTO cdDTO = managerMapper.getCdByNo(cdNo);
		   ResponseEntity<byte[]> image = null;
		   try {
			   File detailImg = new File(cdDTO.getDetailImg());
			   System.out.println("디테일이미지" + detailImg);
			   if (detailImg.exists()) {
				   FileInputStream inputStream = new FileInputStream(detailImg);
				   byte[] imageBytes = IOUtils.toByteArray(inputStream);
				   image = new ResponseEntity<>(imageBytes, HttpStatus.OK);
			   } 
		   } catch(Exception e) {
			   e.printStackTrace();
		   }
		   return image;
	   }
	   
	   
    @Override
		public void getOrderList(HttpServletRequest request, Model model) {
		
    	int sum = managerMapper.getOrderSum();
    	
    	Optional<String> opt1 = Optional.ofNullable(request.getParameter("query"));
    	String query = opt1.orElse("");
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("query", query);
    	
    	Optional<String> opt2 = Optional.ofNullable(request.getParameter("page"));
    	int page = Integer.parseInt(opt2.orElse("1"));
    	
    	int totalRecord = managerMapper.getOrderCount();
    	
    	int recordPerPage = 5;
    	
    	pageUtil.setPageUtil(page, totalRecord, recordPerPage);
    	
    	
    	map.put("begin", pageUtil.getBegin());
    	map.put("recordPerPage", recordPerPage);
    	
    	List<OrderDTO> orderList = managerMapper.getOrderList(map);
    	model.addAttribute("orderList", orderList);
    	model.addAttribute("pagination", pageUtil.getPagination("/manager/sale.html?&query=" + query));
    	model.addAttribute("sum", sum);
	}

	
}
