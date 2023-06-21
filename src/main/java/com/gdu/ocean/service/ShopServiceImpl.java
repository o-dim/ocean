package com.gdu.ocean.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.CartDetailDTO;
import com.gdu.ocean.domain.CdDTO;
import com.gdu.ocean.domain.HashtagDTO;
import com.gdu.ocean.domain.UsersDTO;
import com.gdu.ocean.mapper.ShopMapper;
import com.gdu.ocean.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements ShopService {

	private final ShopMapper shopMapper;
	private final PageUtil pageUtil;
	
	@Override
	public void getCdList(HttpServletRequest request, Model model) {
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("type"));
		String type = opt1.orElse("");

		//파라미터 query가 전달되지 않는 경우 query=""로 처리
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("searchText1"));
		String searchText1 = opt2.orElse("");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("searchText1", searchText1);
		System.out.println(map);
		
		
		Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt3.orElse("1"));
		
		int totalRecord = shopMapper.getCdCount();
		int recordPerPage = 20;
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);

		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<CdDTO> cdList = shopMapper.getCdList(map);
		model.addAttribute("cdList", cdList);
		model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
		model.addAttribute("pagination", pageUtil.getPagination("/shop/list.do?searchText1" + searchText1));
	}
	
	@Override
	public List<CartDetailDTO> cartDetailList(HttpServletRequest request, Model model) {
		
		int cartNo = Integer.parseInt(request.getParameter("cartNo"));
		List<CartDetailDTO> cartDetail = shopMapper.getCartDetailNo(cartNo);
		model.addAttribute("cartNo", cartNo);
		return cartDetail;
	}
	
	@Override
	public void getCdByNo(int cdNo, Model model) {
		model.addAttribute("cd", shopMapper.getCdByNo(cdNo));
	}
	
	@Override
	public List<HashtagDTO> getHashtagName(int cdNo) {
		return shopMapper.getHashtagName(cdNo);
	}
	
	@Override
	public Map<String, Object> getCartNo(HttpServletRequest request) {
		String userNo = request.getParameter("userNo");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("userNo", userNo);
		return map;
	} 
	
	
}
	

