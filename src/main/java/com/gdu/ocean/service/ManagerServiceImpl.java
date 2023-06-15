package com.gdu.ocean.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.ocean.domain.CdDTO;
import com.gdu.ocean.domain.HashtagDTO;
import com.gdu.ocean.domain.UsersDTO;
import com.gdu.ocean.mapper.ManagerMapper;
import com.gdu.ocean.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

	private final ManagerMapper managerMapper;
	private final PageUtil pageUtil;
	
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
		System.out.println("시디 번호 " + cdNos);
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
	
	/*
	 * @Override public void addCd(HttpServletRequest request, HttpServletResponse
	 * response) {
	 * 
	 * String title = request.getParameter("title"); int price =
	 * Integer.parseInt(request.getParameter("price")); int htNo =
	 * Integer.parseInt(request.getParameter("htNo"));
	 * 
	 * HashtagDTO hashtagDTO = new HashtagDTO(); hashtagDTO.setHtNo(htNo); CdDTO
	 * cdDTO = new CdDTO(); cdDTO.setTitle(title); cdDTO.setPrice(price);
	 * 
	 * int addResult = managerMapper.addCd(cdDTO);
	 * 
	 * try {
	 * 
	 * response.setContentType("text/html; charset=UTF-8"); PrintWriter out =
	 * response.getWriter(); out.println("<script>"); if(addResult == 1) {
	 * out.println("alert('작성되었습니다.');");
	 * out.println("location.href='/admin/salelist.do';"); } else {
	 * out.println("alert('작성이 실패했습니다.');"); out.println("history.back();"); }
	 * out.println("</script>"); out.flush(); out.close();
	 * 
	 * } catch(Exception e) { e.printStackTrace(); }
	 * 
	 * }
	 */

	
	
	@Override
	public void getUserListPagination(HttpServletRequest request, Model model) {
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt1.orElse("1"));
		
		int totalRecord = managerMapper.getUserCount();
		
		int recordPerPage = 5;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<UsersDTO> userList = managerMapper.getUserListPagination(map);
		
		model.addAttribute("userList", userList);
		model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/admin/member.html"));
		model.addAttribute("userNo", totalRecord - (page - 1) * recordPerPage);
		
		
	}
	
	
	
}
