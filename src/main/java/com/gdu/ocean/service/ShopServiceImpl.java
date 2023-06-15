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
	
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt.orElse("1"));
		
		int totalRecord = shopMapper.getCdCount();
		
		int recordPerPage = 20;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<CdDTO> cdList = shopMapper.getCdList(map);
		model.addAttribute("cdList", cdList);
		model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
		model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/shop/list.do"));
	
	}
	
	@Override
	public void getCdByNo(int cdNo, Model model) {
		model.addAttribute("cd", shopMapper.getCdByNo(cdNo));
		
	}
	
	@Override
	public void getCartListFK(int cartNo, int cdNo, Model model) {
		model.addAttribute("cartCdNo", shopMapper.getCartListFK(cartNo, cdNo));
	}
	
	@Override
	public void getcartList(HttpServletRequest request, Model model) {
		List<CartDetailDTO> cartList = shopMapper.getCartList();
		model.addAttribute("cartList", cartList);
	}
	
	
}
	

